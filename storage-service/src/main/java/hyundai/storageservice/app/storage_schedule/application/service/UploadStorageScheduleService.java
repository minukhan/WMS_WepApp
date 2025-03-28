package hyundai.storageservice.app.storage_schedule.application.service;

import hyundai.storageservice.app.storage.application.entity.Storage;
import hyundai.storageservice.app.storage_schedule.adapter.dto.fein.*;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import hyundai.storageservice.app.storage_schedule.application.port.in.UploadStorageScheduleUseCase;
import hyundai.storageservice.app.storage_schedule.application.port.out.UploadStorageSchedulePort;
import hyundai.storageservice.app.storage_schedule.application.port.out.fein.PartFeinClient;
import hyundai.storageservice.app.storage_schedule.application.port.out.fein.PurchaseFeinClient;
import hyundai.storageservice.app.storage_schedule.application.port.out.fein.SupplyFeinClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UploadStorageScheduleService implements UploadStorageScheduleUseCase {

    private final UploadStorageSchedulePort uploadStorageSchedualPort;
    private final PartFeinClient partFeinClient;
    private final SupplyFeinClient supplyFeinClient;
    private final PurchaseFeinClient purchaseFeinClient;

    @Override
    public void uploadStorageScheduleUseCase() {

        //storage에 저장되어있는 partId 추출
        List<Storage> storages = uploadStorageSchedualPort.findAll();
        List<String> storagePartIds = storages.stream()
                .map(storage -> storage.getPartId())
                .collect(Collectors.toList());

        //fein 으로 supply에서 partId 추출
        MonthlyTopPartsResponseDto supplyResponse = supplyFeinClient.getSupply(2025,2);
        List<String> top10PartIds;

        List<TopPartDetail> topPartDetails = supplyResponse.topParts();

        if(topPartDetails == null) {
            top10PartIds = new ArrayList<>();
        } else {
            top10PartIds = topPartDetails.stream()
                    .map(topPartDetail -> topPartDetail.partId()).collect(Collectors.toList());

        }

//        top10PartIds.add("DHP056");
//        top10PartIds.add("DHP057");
//        top10PartIds.add("RHP126");
//        top10PartIds.add("RHP127");
//        top10PartIds.add("RHP128");
//        top10PartIds.add("SHP178");
//        top10PartIds.add("RHP179");
//        top10PartIds.add("RHP180");
//        top10PartIds.add("RHP181");
//        top10PartIds.add("RHP182");
//        top10PartIds.add("XHP417");
//        top10PartIds.add("XHP418");
//        top10PartIds.add("XHP419");
//        top10PartIds.add("XHP420");

        //  차집합 연산 (List.removeIf 사용)
        top10PartIds.removeIf(storagePartIds::contains);

        //차집합 후 partId 를 이용해서 inventoryList 들을 가져옴.
        InventoryStorageResponse inventoryStorageResponse = partFeinClient.findPartInventoryByPartIds(top10PartIds);

        //저장할 list 생성
        List<InventorySectionDto> storeLists = new ArrayList<>();

        // 앞에 있는 inventory 인지 아닌지 판단하는 로직
        inventoryStorageResponse.inventorySectionDtos().stream()
                .forEach(inventorySectionDto -> {
                    if (!FrontSection(inventorySectionDto.sectionName())) {
                        storeLists.add(inventorySectionDto);
                    }
                });

        int sumQuantity = 0;
        for (InventorySectionDto storeList : storeLists) {
            sumQuantity += storeList.partQuantity();
        }

        // 입고 예정 고려해야하는 데이터
        ResponseEntity<ExpectedQuantityTomorrowResponse> response = purchaseFeinClient.getExpectedStockQuantityTomorrow();
        List<ExpectedQuantityTomorrowDto> expectedQuantityTomorrowDtos = response.getBody().expectedQuantityList();

        //section의 정보들을 가져옴.
        List<SectionDto> sectionDtos = partFeinClient.findAllStorePosition().sectionDtos();


        // 3. 각 section별로 현재 수량 + 내일 입고 예정량을 반영한 남은 수용량 계산
        //    (남은 수용량 = maxCapacity - (현재 수량 + 내일 입고 예정량))
        Map<Long, Integer> availableCapacityMap = new HashMap<>();
        for (SectionDto section : sectionDtos) {
            int expectedIncoming = expectedQuantityTomorrowDtos.stream()
                    .filter(e -> e.sectionId().equals(section.sectionId()))
                    .mapToInt(ExpectedQuantityTomorrowDto::quantity)
                    .sum();
            int effectiveCapacity = section.maxCapacity() - (section.quantity() + expectedIncoming);
            availableCapacityMap.put(section.sectionId(), effectiveCapacity);
        }

        // 4. storeLists(적재할 재고 목록)에 대해, 각 재고를 적재할 수 있는 section을 찾아 스케줄 생성
        for (InventorySectionDto storeList : storeLists) {
            // 적재 가능한 섹션 찾기 (수용량 많은 순)
            Optional<SectionDto> targetSectionOpt = sectionDtos.stream()
                    .filter(section -> availableCapacityMap.getOrDefault(section.sectionId(), 0) >= storeList.partQuantity())
                    .sorted(Comparator.comparingInt(s -> availableCapacityMap.getOrDefault(s.sectionId(), 0)))
                    .findFirst();

            if (targetSectionOpt.isPresent()) {
                SectionDto targetSection = targetSectionOpt.get();

                // 적치 스케줄 생성
                StorageSchedule storageSchedule = StorageSchedule.builder()
                        .partId(storeList.partId())
                        .partQuantity(storeList.partQuantity())
                        .storageQuantity(0)
                        .fromLocationSectionName(storeList.sectionName())
                        .fromLocationSectionFloor(storeList.floor())
                        .toLocationSectionName(targetSection.sectionName())
                        .toLocationSectionFloor(targetSection.floor())
                        .createdAt(LocalDate.now())
                        .scheduledAt(LocalDate.now())
                        .status("PENDING")
                        .build();

                uploadStorageSchedualPort.save(storageSchedule);

                // 수용량 업데이트
                availableCapacityMap.put(targetSection.sectionId(), availableCapacityMap.get(targetSection.sectionId()) - storeList.partQuantity());
            }
        }

    }


    // section name 이 A~E 일경우 그대로 진행.
    private boolean FrontSection(String sectionName) {
        return sectionName.matches(".*[A-E].*");
    }
}
