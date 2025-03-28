package site.autoever.verifyservice.verify.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.autoever.verifyservice.verify.adapter.in.dto.OrderPartRequest;
import site.autoever.verifyservice.verify.adapter.in.dto.VerifyOrderRequest;
import site.autoever.verifyservice.verify.adapter.in.dto.VerifyOrderResponse;
import site.autoever.verifyservice.verify.application.domain.VerificationMessage;
import site.autoever.verifyservice.verify.application.port.in.VerifyOrderUseCase;
import site.autoever.verifyservice.verify.application.port.out.GetPartStockPort;
import site.autoever.verifyservice.verify.application.port.out.GetPurchaseStockPort;
import site.autoever.verifyservice.verify.application.port.out.GetSupplyStockPort;
import site.autoever.verifyservice.verify.application.port.out.dto.PartInfoDto;
import site.autoever.verifyservice.verify.application.port.out.dto.PartResponseDto;
import site.autoever.verifyservice.verify.application.port.out.dto.PartRequestDto;
import site.autoever.verifyservice.verify.application.port.out.dto.PurchaseInfoDto;
import site.autoever.verifyservice.verify.application.port.out.dto.PurchaseResponseDto;
import site.autoever.verifyservice.verify.application.port.out.dto.PurchaseRequestDto;
import site.autoever.verifyservice.verify.application.port.out.dto.SupplyInfoDto;
import site.autoever.verifyservice.verify.application.port.out.dto.SupplyResponseDto;
import site.autoever.verifyservice.verify.application.port.out.dto.SupplyRequestDto;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerifyOrderService implements VerifyOrderUseCase {

    private final GetPartStockPort partStockPort;
    private final GetSupplyStockPort supplyStockPort;
    private final GetPurchaseStockPort purchaseStockPort;

    @Override
    public VerifyOrderResponse verifyOrder(VerifyOrderRequest request) {
        log.info("🚀 주문 검증 시작 - Order ID: {}", request.orderId());

        // 추가: dueDate가 현재 날짜보다 이전이거나 같은 경우 즉시 실패 처리
        if (!request.dueDate().isAfter(LocalDate.now())) {
            log.error("❌ 주문 마감일이 현재 날짜와 같거나 이전입니다 - 주문 마감일: {}", request.dueDate());
            return new VerifyOrderResponse(false, VerificationMessage.FAILURE_DUE_DATE_TIME_EXCEED.getMessage());
        }


        // 0) 현재 날짜 검증 (필요에 따라 MonthDay 사용)
        MonthDay now = MonthDay.now();
        MonthDay dueDate = MonthDay.from(request.dueDate());

        // 1) 외부 서비스로부터 재고 정보 조회
        PartResponseDto partResponse = partStockPort.getPartStocks(PartRequestDto.from(request));
        SupplyResponseDto supplyResponse = supplyStockPort.getSupplyStocks(SupplyRequestDto.from(request));
        PurchaseResponseDto purchaseResponse = purchaseStockPort.getPurchaseStocks(PurchaseRequestDto.from(request));

        log.info("📦 창고 재고 정보 수신 완료: {}", partResponse);
        log.info("📦 출고 예정 재고 정보 수신 완료: {}", supplyResponse);
        log.info("📦 입고 예정 재고 정보 수신 완료: {}", purchaseResponse);

        // 각 파트별 재고/배송 정보 매핑 (Key: partId)
        Map<String, Long> partMap = partResponse.partInfos().stream()
                .collect(Collectors.toMap(PartInfoDto::partId, PartInfoDto::quantity));

        Map<String, Long> partDeliveryMap = partResponse.partInfos().stream()
                .collect(Collectors.toMap(PartInfoDto::partId, PartInfoDto::deliveryDuration));

        Map<String, Long> supplyMap = supplyResponse.supplyInfos().stream()
                .collect(Collectors.toMap(SupplyInfoDto::partId, SupplyInfoDto::quantity));

        Map<String, Long> purchaseMap = purchaseResponse.purchaseInfos().stream()
                .collect(Collectors.toMap(PurchaseInfoDto::partId, PurchaseInfoDto::quantity));

        // 플래그: 하나라도 부족한 부품이 있었는지
        boolean anyInsufficient = false;

        for (OrderPartRequest orderPartRequest : request.parts()) {
            String partId = orderPartRequest.partId();
            long warehouseQuantity = partMap.getOrDefault(partId, 0L);
            long purchaseQuantity = purchaseMap.getOrDefault(partId, 0L);
            long supplyQuantity = supplyMap.getOrDefault(partId, 0L);
            long deliveryDays = partDeliveryMap.getOrDefault(partId, 0L);

            long calculatedQuantity = warehouseQuantity + purchaseQuantity - supplyQuantity;

            log.info("🛠 검증 시작 - Part ID: {} | 요청 수량: {} | 창고 수량: {} | 입고 예정: {} | 출고 예정: {} | 계산된 총량: {} (계산식: {} + {} - {})",
                    partId, orderPartRequest.quantity(), warehouseQuantity, purchaseQuantity, supplyQuantity, calculatedQuantity,
                    warehouseQuantity, purchaseQuantity, supplyQuantity);

            // 재고 부족인 경우
            if (orderPartRequest.quantity() > calculatedQuantity) {
                anyInsufficient = true;
                boolean canDeliver = canDelivery(request.orderDate(), request.dueDate(), deliveryDays);
                boolean hasCapacity = isWarehouseCapacitySufficient(partResponse, orderPartRequest, request);

                if (canDeliver && hasCapacity) {
                    log.info("✅ 기한 내 배송 가능 및 창고 용량 충분 (입고 처리로 보완) - Part ID: {}", partId);
                    // 부족한 부품이 있으나 입고 처리를 통해 보완 가능한 경우는 계속 진행
                } else {
                    if (!canDeliver) {
                        log.error("❌ 기한 내 배송 불가 - Part ID: {}", partId);
                        return new VerifyOrderResponse(false,
                                VerificationMessage.FAILURE_DELIVERY_TIME_EXCEEDED.getMessage() + " - Part ID: " + partId);
                    }
                    if (!hasCapacity) {
                        log.error("❌ 창고 용량 부족 - Part ID: {}", partId);
                        return new VerifyOrderResponse(false,
                                VerificationMessage.FAILURE_INSUFFICIENT_WAREHOUSE_CAPACITY.getMessage() + " - Part ID: " + partId);
                    }
                }
            }
        }

        // 최종 반환: 하나라도 부족한 부품이 있었다면 입고처리 보완 메시지, 아니면 재고 충분 메시지
        if (anyInsufficient) {
            return new VerifyOrderResponse(true, VerificationMessage.SUCCESS_INVENTORY_INSUFFICIENT_BUT_PROCESSABLE.getMessage());
        } else {
            return new VerifyOrderResponse(true, VerificationMessage.SUCCESS_INVENTORY_SUFFICIENT.getMessage());
        }
    }

    private boolean canDelivery(LocalDate orderDate, LocalDate dueDate, long deliveryDays) {
        LocalDate calculatedDate = orderDate.plusDays(deliveryDays);
        MonthDay preparedMonthDay = MonthDay.from(calculatedDate);
        MonthDay dueMonthDay = MonthDay.from(dueDate);

        log.info("📅 예상 배송 날짜 계산 - 배송 소요일: {}일 | 준비 완료일: {} ({}월 {}일) | 주문 마감일: {} ({}월 {}일)",
                deliveryDays, calculatedDate, preparedMonthDay.getMonthValue(), preparedMonthDay.getDayOfMonth(),
                dueDate, dueMonthDay.getMonthValue(), dueMonthDay.getDayOfMonth());

        if (preparedMonthDay.isAfter(dueMonthDay)) {
            log.error("❌ 기한 내 배송 불가 - 준비 완료일: {} ({}월 {}일) | 주문 마감일: {} ({}월 {}일)",
                    calculatedDate, preparedMonthDay.getMonthValue(), preparedMonthDay.getDayOfMonth(),
                    dueDate, dueMonthDay.getMonthValue(), dueMonthDay.getDayOfMonth());
            return false;
        }
        return true;
    }

    private boolean isWarehouseCapacitySufficient(PartResponseDto partResponse, OrderPartRequest orderPartRequest, VerifyOrderRequest request) {
        boolean isSingleOrderCapacitySufficient = isSingleOrderWithinCapacity(partResponse, orderPartRequest);
        boolean isTotalOrderCapacitySufficient = isTotalOrderWithinCapacity(partResponse, request);
        return isSingleOrderCapacitySufficient && isTotalOrderCapacitySufficient;
    }

    private boolean isSingleOrderWithinCapacity(PartResponseDto partResponse, OrderPartRequest orderPartRequest) {
        boolean isCapacitySufficient = partResponse.warehouseCapacity() + orderPartRequest.quantity() <= partResponse.warehouseMaxCapacity();
        log.info("🏭 단일 주문 창고 용량 확인 - 현재 창고 수용량: {} | 요청 후 예상 수용량: {} | 창고 최대 용량: {} | 결과: {}",
                partResponse.warehouseCapacity(),
                partResponse.warehouseCapacity() + orderPartRequest.quantity(),
                partResponse.warehouseMaxCapacity(),
                isCapacitySufficient ? "✅ 충분" : "❌ 초과");
        return isCapacitySufficient;
    }

    private boolean isTotalOrderWithinCapacity(PartResponseDto partResponse, VerifyOrderRequest request) {
        boolean isCapacitySufficient = partResponse.warehouseCapacity() + request.total() <= partResponse.warehouseMaxCapacity();
        log.info("🏭 전체 주문 창고 용량 확인 - 현재 창고 수용량: {} | 전체 주문 후 예상 수용량: {} | 창고 최대 용량: {} | 결과: {}",
                partResponse.warehouseCapacity(),
                partResponse.warehouseCapacity() + request.total(),
                partResponse.warehouseMaxCapacity(),
                isCapacitySufficient ? "✅ 충분" : "❌ 초과");
        return isCapacitySufficient;
    }
}
