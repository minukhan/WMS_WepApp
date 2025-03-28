package hyundai.autostockservice.autoStock.application.service;

import hyundai.autostockservice.autoStock.adapter.dto.feign.PartDto;
import hyundai.autostockservice.autoStock.adapter.dto.feign.PartIdAndQuantityDto;
import hyundai.autostockservice.autoStock.adapter.dto.feign.PurchaseRequestSaveRequest;
import hyundai.autostockservice.autoStock.adapter.dto.feign.StockRequest;
import hyundai.autostockservice.autoStock.application.port.out.feign.PartServiceClient;
import hyundai.autostockservice.autoStock.application.port.out.feign.PurchaseServiceClient;
import hyundai.autostockservice.autoStock.application.port.out.feign.SupplyServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreateAutoStockService {

    private final PartServiceClient partServiceClient;
    private final SupplyServiceClient supplyServiceClient;
    private final PurchaseServiceClient purchaseServiceClient;

    @Scheduled(cron = "0 0 0 * * *")
    public void autoStockService() {
        // 전체 부품 조회
        List<PartDto> allParts = partServiceClient.getAllParts(null, 450, null).content();

        // 배송에 걸리는 시간별로 그룹핑
        Map<Integer, List<PartDto>> partMappedByDeliveryDuration = allParts.stream()
                .collect(Collectors
                        .groupingBy(PartDto::deliveryDuration)
                );

        // 자동 납품할 요청서 리스트
        Map<String, Integer> autoStockRequestList = new HashMap<>();

        // 1. 주문서 요청에 맞춰 주문
        // 2. 안전재고 미만인 재고 주문
        checkPartOrder(partMappedByDeliveryDuration, autoStockRequestList);

        // 부품 요청서 저장을 위한 purchase 서비스 호출
        if(!autoStockRequestList.isEmpty()){
            List<PartIdAndQuantityDto> requestList = autoStockRequestList.entrySet().stream()
                .map(request ->
                        new PartIdAndQuantityDto(request.getKey(), request.getValue())
                ).toList();
            purchaseServiceClient.saveAutoStock(new PurchaseRequestSaveRequest(requestList));
        }
    }


    public void checkPartOrder(Map<Integer, List<PartDto>> partMappedByDeliveryDuration, Map<String, Integer> requestList) {
        // 배송 기간별로 나누어 조회
        for(Integer date : partMappedByDeliveryDuration.keySet()){
            LocalDate targetDate = LocalDate.now().plusDays(date);
            List<String> totalPartIds = partMappedByDeliveryDuration.get(date).stream()
                    .map(PartDto::partId)
                    .toList();

            // 현재로부터 배송 기간일 후에 출고 스케줄이 있는 품목의 코드와 출고 수량
            Map<String, Integer> reservedOrder =
                    supplyServiceClient.getQuantityRequested(new StockRequest(targetDate, totalPartIds)).stream()
                            .filter(dto -> dto.quantity() > 0)
                            .collect(Collectors.toMap(PartIdAndQuantityDto::partId, PartIdAndQuantityDto::quantity));

            if(reservedOrder.isEmpty()) continue;

            List<String> requestedPartIds = reservedOrder.keySet().stream().toList();
            StockRequest stockRequest = new StockRequest(targetDate, requestedPartIds);

            // 요청이 있는 품목에 대한 info, 기간 내 출고되는 총 수량, 입고되는 총 수량
            Map<String, PartDto> partDtoMap = partMappedByDeliveryDuration.get(date).stream()
                    .collect(Collectors.toMap(PartDto::partId, p -> p));
            Map<String, Integer> stockQuantityList = supplyServiceClient.getStockAmountUntilDate(stockRequest).parts().stream()
                    .collect(Collectors.toMap(PartIdAndQuantityDto::partId, PartIdAndQuantityDto::quantity));
            Map<String, Integer> storeQuantityList = purchaseServiceClient.getStockAmountUntilDate(stockRequest).parts().stream()
                    .collect(Collectors.toMap(PartIdAndQuantityDto::partId, PartIdAndQuantityDto::quantity));

            // 출고 당일 예상 수량 계산
            requestedPartIds.forEach(partId -> {
                PartDto part = partDtoMap.get(partId);
                int expectedQuantity = part.quantity() - stockQuantityList.get(partId) + storeQuantityList.get(partId);

                // 1. 당일에 출고할 부품 수 부족(주문서 만족 불가)!!! -> schedule에 등록
                if(expectedQuantity < 0){
                    requestList.put(partId, reservedOrder.get(partId));
                    expectedQuantity += reservedOrder.get(partId);
                }

                // 2. 출고 당일 안전 재고 아래로 떨어지는 경우
                if(expectedQuantity < part.safetyStock()){
                    requestList.merge(partId, part.optimalStock() - expectedQuantity, Integer::sum);
                }
            });
        }
    }


}
