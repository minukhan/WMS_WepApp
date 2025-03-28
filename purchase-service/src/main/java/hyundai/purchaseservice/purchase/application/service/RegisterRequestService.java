package hyundai.purchaseservice.purchase.application.service;

import hyundai.purchaseservice.infrastructure.util.UserIdResolver;
import hyundai.purchaseservice.purchase.adapter.in.dto.RegisterAutoRequest;
import hyundai.purchaseservice.purchase.adapter.in.dto.RegisterRequest;
import hyundai.purchaseservice.purchase.adapter.out.dto.PartIdAndQuantityResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.SaveRequestDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.StoreDeliveryAmountBySectionIdDto;
import hyundai.purchaseservice.purchase.adapter.out.dto.feign.*;
import hyundai.purchaseservice.purchase.application.exception.InvalidInputException;
import hyundai.purchaseservice.purchase.application.exception.RequestExistsException;
import hyundai.purchaseservice.purchase.application.exception.RequestTooManyException;
import hyundai.purchaseservice.purchase.application.port.in.RegisterRequestUseCase;
import hyundai.purchaseservice.purchase.application.port.out.RegisterRequestPort;
import hyundai.purchaseservice.purchase.application.port.out.feign.AlarmServiceClient;
import hyundai.purchaseservice.purchase.application.port.out.feign.LogServiceClient;
import hyundai.purchaseservice.purchase.application.port.out.feign.PartServiceClient;
import hyundai.purchaseservice.purchase.application.port.out.feign.SupplyServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegisterRequestService implements RegisterRequestUseCase {

    private final RegisterRequestPort registerRequestPort;
    private final PartServiceClient partServiceClient;
    private final SupplyServiceClient supplyServiceClient;
    private final LogServiceClient logServiceClient;
    private final AlarmServiceClient alarmServiceClient;
    private final UserIdResolver userIdResolver;

    @Override
    public void registerRequest(RegisterRequest registerRequest) {
        if (registerRequest.name().equals("자동 발주")){
            throw new InvalidInputException(registerRequest.name());
        }

        // 당일 같은 품목을 요청한 이력이 있는 경우 exception
        Long count = registerRequestPort.checkRequestExist(registerRequest.partId(), LocalDate.now());
        if (count > 0){
            throw new RequestExistsException(registerRequest.partId());
        }

        PartWithSupplierDto part = partServiceClient.getPartInfo(registerRequest.partId()).partSupplierDto();
        Long partPrice = part.price();
        LocalDate arrivalDate = LocalDate.now().plusDays(part.deliveryDuration());

        /* 입고 검증 */
        Long totalStore = registerRequestPort.getTotalStoreAmountUntilArrivalDate(arrivalDate);
        Long totalDelivery = supplyServiceClient.getTotalQuantity(arrivalDate.toString());
        SpaceResponse space = partServiceClient.getSpaceInfo();
        int left = Long.valueOf(space.emptySpace() - totalStore + totalDelivery).intValue();

        if(registerRequest.quantity() > left){
            throw new RequestTooManyException(registerRequest.quantity());
        }

        long[] storeQuantity = {registerRequest.quantity()};
        List<List<Long>> scheduleList = new ArrayList<>();

        createScheduleFromRequest(registerRequest.partId(), storeQuantity, arrivalDate, scheduleList);

        // 자리를 못 구하는 경우 exception 발생
        if(storeQuantity[0] > 0){
            throw new RequestTooManyException(registerRequest.quantity());
        }

        // db에 요청서 저장 + 입고 스케줄 등록
        SaveRequestDto saveRequestDto = SaveRequestDto.of(registerRequest, partPrice, LocalDate.now(), arrivalDate);
        registerRequestPort.save(saveRequestDto, scheduleList);

        logServiceClient.save(
                new LogRequest(userIdResolver.getCurrentUserId(),
                        "[부품 요청] 관리자"  + registerRequest.name()+"이(가) 품목 코드 "+registerRequest.partId()+" 요청서를 작성하였습니다.")
        );
    }

    @Override
    public void registerAutoRequest(RegisterAutoRequest registerAutoRequest) {
        // part 서비스로부터 배송 기간, 품목 금액 조회
        List<String> partIds = registerAutoRequest.requests().stream().map(PartIdAndQuantityResponse::partId).toList();
        Map<String, PartWithSupplierDto> partInfo = partServiceClient.getPartInfoList(partIds).partDtos().stream()
                .collect(Collectors.toMap(PartWithSupplierDto::partId, p -> p));

        // 요청서마다 스케줄 생성
        int success = 0;
        int failure = 0;
        long[] storeQuantity = new long[1];
        List<List<Long>> scheduleList = new ArrayList<>();
        
        for(PartIdAndQuantityResponse request : registerAutoRequest.requests()){
            storeQuantity[0] = request.quantity();
            scheduleList.clear();

            String requestedPartId = request.partId();
            LocalDate orderedAt = LocalDate.now();
            LocalDate arrivalDate =orderedAt.plusDays(partInfo.get(requestedPartId).deliveryDuration());
            createScheduleFromRequest(requestedPartId, storeQuantity, arrivalDate, scheduleList);

            if(storeQuantity[0] > 0){
                failure++;
            }
            else{
                // db에 요청서 저장 + 입고 스케줄 등록
                SaveRequestDto saveRequestDto = SaveRequestDto.of(request, partInfo.get(requestedPartId).price(), orderedAt, arrivalDate);
                registerRequestPort.save(saveRequestDto, scheduleList);
                success++;
            }
        }

        // 알람 발생
        if(success > 0){
            alarmServiceClient.alarm(new AlarmRequest(
                    "ROLE_INFRA_MANAGER",
                    "[자동 발주] "+ success +"개의 부품 요청서가 자동으로 작성됐습니다.",
                    "PURCHASE"
            ));
        }
        if(failure > 0){
            alarmServiceClient.alarm(new AlarmRequest(
                    "ROLE_INFRA_MANAGER",
                    "[자동 발주] "+ failure +"개의 부품 요청서 작성에 실패했습니다.",
                    "PURCHASE"
            ));
        }
    }


    public void createScheduleFromRequest(String partId, long[] storeQuantity, LocalDate arrivalDate, List<List<Long>> scheduleList){
        int sectionMaxQuantity = 108;

        // 현재 부품이 존재하는 위치 정보 가져온다.
        List<InventorySectionDto> partInventory = partServiceClient.getInvetoryInfoOfPart(partId).inventoryDtoList();

        // 미래의 구역 상황 계산
        List<InventorySectionDto> futureInventory = makeFutureInventory(partInventory, partId, arrivalDate);

        // 입고 위치와 몇개 저장할 건지를 리스트로 구한다.
        storePartInSection(futureInventory, sectionMaxQuantity, storeQuantity, scheduleList);

        // 새로운 위치에 입고가 필요한 경우
        if(storeQuantity[0] > 0){
            storePartInNewSection(arrivalDate, storeQuantity, scheduleList);
        }
    }

    // 미래의 구역 상황
    public List<InventorySectionDto> makeFutureInventory(List<InventorySectionDto> partInventory, String partId, LocalDate arrivalDate){
        // section id에 예정된 입고 수량 리스트
        List<Long> sectionIds = partInventory.stream().map(InventorySectionDto::sectionId).toList();
        List<StoreDeliveryAmountBySectionIdDto> storeUntilArrival = registerRequestPort.storeUntilArrival(sectionIds, arrivalDate);

        // 출고될 part id 수량
        AtomicInteger stockUntilArrival = new AtomicInteger(
                supplyServiceClient.getExpectedStock(
                        new ExpectedStockRequest(arrivalDate, List.of(partId))).total()
        );

        // 현재 구역 정보와 입고, 출고 수량 합산해서 미래의 구역 상태 계산
        return partInventory.stream()
                .sorted(Comparator.comparing(InventorySectionDto::inventoryId))
                .map(inventory -> {
                    AtomicReference<Integer> futureQuantity = new AtomicReference<>(inventory.sectionQuantity());
                    AtomicReference<Integer> futurePartQuantity = new AtomicReference<>(inventory.partQuantity());

                    // 입고 수량 더하기
                    storeUntilArrival.stream()
                            .filter(store-> store.sectionId().equals(inventory.sectionId()))
                            .forEach(store->{
                                futureQuantity.updateAndGet(v -> v + store.quantity());
                                if(store.partId().equals(partId)){
                                    futurePartQuantity.updateAndGet(v -> v + store.quantity());
                                }
                            });
                    // 출고 수량 빼기
                    if(stockUntilArrival.get() != 0){
                        int stockQuantity = Math.min(inventory.partQuantity(), stockUntilArrival.get());

                        futurePartQuantity.updateAndGet(v -> v - stockQuantity);
                        futureQuantity.updateAndGet(v -> v - stockQuantity);
                        stockUntilArrival.addAndGet(-stockQuantity);
                    }
                    return InventorySectionDto.newInventory(inventory, futurePartQuantity.get(), futureQuantity.get());
                }).toList();
    }


    // 기존 구역에 입고
    public void storePartInSection(List<InventorySectionDto> futureInventory, int sectionMaxQuantity, long[] totalStoreQuantity, List<List<Long>> scheduleList){
        // 여유 공간이 있고 부품이 많이 남아있는 구역 순서대로 입고 개수를 구한다
        futureInventory.stream()
                .filter(section -> section.sectionQuantity() < sectionMaxQuantity)
                .sorted(
                        Comparator.comparing(InventorySectionDto::sectionQuantity).reversed()
                                .thenComparing(InventorySectionDto::inventoryId)
                ).forEach(section -> {
                    if(totalStoreQuantity[0] > 0){
                        long storeQuantity = Math.min(sectionMaxQuantity - section.sectionQuantity(), totalStoreQuantity[0]);

                        scheduleList.add(List.of(section.sectionId(), storeQuantity));
                        totalStoreQuantity[0] -= storeQuantity;
                    }
                });
    }


    // 새로운 구역에 입고
    public void storePartInNewSection(LocalDate arrivalDate, long[] totalStoreQuantity, List<List<Long>> scheduleList){
        // 구역 정보를 가져온다
        List<SectionDto> sectionInfo = partServiceClient.getAllSectionInfoList().sectionDtos();

        // 구역별로 예상 날짜까지 입고될 수량을 구한다
        Map<Long, Integer> allSectionStoreUntilArrival = registerRequestPort.allSectionStoreUntilArrival(arrivalDate).stream()
                .collect(
                        Collectors.toMap(
                                StoreDeliveryAmountBySectionIdDto::sectionId,
                                StoreDeliveryAmountBySectionIdDto::quantity)
                );

        // arrivalDate일 때 구역 여유 공간을 구한다.
        List<List<Long>> futureSections = sectionInfo.stream()
                .map(section -> {
                    long leftSpace = section.maxCapacity() - section.quantity()
                            - (allSectionStoreUntilArrival.get(section.sectionId()) == null ? 0 : allSectionStoreUntilArrival.get(section.sectionId()));

                    return List.of(section.sectionId(), leftSpace);
                }).filter(list -> list.get(1) > 0)
                .sorted((a, b) -> Long.compare(b.get(1), a.get(1))).toList();

        if(futureSections.isEmpty()) return;

        // 여유 공간 안에 남은 수량 넣을 수 있으면 넣기
        futureSections.forEach(section -> {
            if(totalStoreQuantity[0] > 0){
                long storeQuantity = Math.min(totalStoreQuantity[0], section.get(1));

                scheduleList.add(List.of(section.get(0), storeQuantity));
                totalStoreQuantity[0] -= storeQuantity;
            }
        });
    }
}
