package site.autoever.verifyservice.verify.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.autoever.verifyservice.verify.adapter.in.dto.OrderPartRequest;
import site.autoever.verifyservice.verify.adapter.in.dto.VerifyOrderRequest;
import site.autoever.verifyservice.verify.adapter.in.dto.VerifyOrderResponse;
import site.autoever.verifyservice.verify.application.domain.VerificationMessage;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerifyOrderServiceTest {

    @InjectMocks
    private VerifyOrderService verifyOrderService;

    @Mock
    private GetPartStockPort partStockPort;

    @Mock
    private GetPurchaseStockPort purchaseStockPort;

    @Mock
    private GetSupplyStockPort supplyStockPort;

    // ── 승인 케이스 ──
    // 단일 요청
    // 1. 재고 충분
    @DisplayName("단일 부품 요청이 '재고 충분'으로 정상 승인된다.")
    @Test
    void success_verify_single_order_first_case() {
        // given
        LocalDate orderDate = LocalDate.now();
        LocalDate dueDate = orderDate.plusDays(10);
        long orderRequestTotal = 50;
        List<OrderPartRequest> orderParts = List.of(new OrderPartRequest("1", 50));

        VerifyOrderRequest verifyOrderRequest = createVerifyOrderRequest(1L, orderDate, dueDate, orderRequestTotal, orderParts);

        PartResponseDto expectedPartResponseDto = createPartResponseDto(
                500, 600, 100, List.of(new PartInfoDto("1", 100, 3))
        );
        SupplyResponseDto expectedSupplyResponseDto = createSupplyResponseDto(
                50, List.of(new SupplyInfoDto("1", 50))
        );
        PurchaseResponseDto expectedPurchaseResponseDto = createPurchaseResponseDto(
                40, List.of(new PurchaseInfoDto("1", 40))
        );

        // when
        when(partStockPort.getPartStocks(new PartRequestDto(List.of("1"))))
                .thenReturn(expectedPartResponseDto);
        when(supplyStockPort.getSupplyStocks(new SupplyRequestDto(dueDate, List.of("1"))))
                .thenReturn(expectedSupplyResponseDto);
        when(purchaseStockPort.getPurchaseStocks(new PurchaseRequestDto(dueDate, List.of("1"))))
                .thenReturn(expectedPurchaseResponseDto);

        VerifyOrderResponse actualResult = verifyOrderService.verifyOrder(verifyOrderRequest);

        // then
        assertTrue(actualResult.status());
        // 재고 부족 조건이 없으므로 "재고 충분" 성공 메시지 기대
        assertEquals(VerificationMessage.SUCCESS_INVENTORY_SUFFICIENT.getMessage(), actualResult.message());
    }

    // 2. 재고 불충분이지만 입고 처리가 가능하여 승인
    @DisplayName("단일 부품 요청이 '재고 불충분'이지만, '입고 기간과 창고 용량이 충분'하여 정상 승인된다.")
    @Test
    void success_verify_single_order_second_case() {
        // given
        LocalDate orderDate = LocalDate.now();
        // Part의 deliveryDays가 3이므로 dueDate를 orderDate.plusDays(3)로 설정 (배송 날짜가 딱 맞음)
        LocalDate dueDate = orderDate.plusDays(3);
        long orderRequestTotal = 60;
        List<OrderPartRequest> orderParts = List.of(new OrderPartRequest("1", 60));

        VerifyOrderRequest verifyOrderRequest = createVerifyOrderRequest(1L, orderDate, dueDate, orderRequestTotal, orderParts);

        // 재고 계산: 100 (창고) + 40 (입고예정) - 90 (출고예정) = 50, 요청은 60 → 부족하지만 입고기간과 창고용량은 충분
        PartResponseDto expectedPartResponseDto = createPartResponseDto(
                500, 600, 100, List.of(new PartInfoDto("1", 100, 3))
        );
        SupplyResponseDto expectedSupplyResponseDto = createSupplyResponseDto(
                50, List.of(new SupplyInfoDto("1", 90))
        );
        PurchaseResponseDto expectedPurchaseResponseDto = createPurchaseResponseDto(
                40, List.of(new PurchaseInfoDto("1", 40))
        );

        // when
        when(partStockPort.getPartStocks(new PartRequestDto(List.of("1"))))
                .thenReturn(expectedPartResponseDto);
        when(supplyStockPort.getSupplyStocks(new SupplyRequestDto(dueDate, List.of("1"))))
                .thenReturn(expectedSupplyResponseDto);
        when(purchaseStockPort.getPurchaseStocks(new PurchaseRequestDto(dueDate, List.of("1"))))
                .thenReturn(expectedPurchaseResponseDto);

        VerifyOrderResponse actualResult = verifyOrderService.verifyOrder(verifyOrderRequest);

        // then
        assertTrue(actualResult.status());
        assertEquals(VerificationMessage.SUCCESS_INVENTORY_INSUFFICIENT_BUT_PROCESSABLE.getMessage(), actualResult.message());
    }

    // 다중 요청
    // 1. 재고 충분
    @DisplayName("2개 이상의 부품 요청이 '재고 충분'으로 정상 승인된다.")
    @Test
    void success_verify_multiple_orders_first_case() {
        // given
        LocalDate orderDate = LocalDate.now();
        LocalDate dueDate = orderDate.plusDays(3);
        long orderRequestTotal = 40;
        List<OrderPartRequest> orderParts = List.of(
                new OrderPartRequest("1", 20),
                new OrderPartRequest("2", 20)
        );

        VerifyOrderRequest verifyOrderRequest = createVerifyOrderRequest(1L, orderDate, dueDate, orderRequestTotal, orderParts);

        PartResponseDto expectedPartResponseDto = createPartResponseDto(
                500, 700, 100, List.of(
                        new PartInfoDto("1", 50, 3),
                        new PartInfoDto("2", 50, 3)
                )
        );
        SupplyResponseDto expectedSupplyResponseDto = createSupplyResponseDto(
                50, List.of(
                        new SupplyInfoDto("1", 25),
                        new SupplyInfoDto("2", 25)
                )
        );
        PurchaseResponseDto expectedPurchaseResponseDto = createPurchaseResponseDto(
                40, List.of(
                        new PurchaseInfoDto("1", 20),
                        new PurchaseInfoDto("2", 20)
                )
        );

        // when
        when(partStockPort.getPartStocks(new PartRequestDto(List.of("1", "2"))))
                .thenReturn(expectedPartResponseDto);
        when(supplyStockPort.getSupplyStocks(new SupplyRequestDto(dueDate, List.of("1", "2"))))
                .thenReturn(expectedSupplyResponseDto);
        when(purchaseStockPort.getPurchaseStocks(new PurchaseRequestDto(dueDate, List.of("1", "2"))))
                .thenReturn(expectedPurchaseResponseDto);

        VerifyOrderResponse actualResult = verifyOrderService.verifyOrder(verifyOrderRequest);

        // then
        assertTrue(actualResult.status());
        assertEquals(VerificationMessage.SUCCESS_INVENTORY_SUFFICIENT.getMessage(), actualResult.message());
    }

    // 2. 재고 불충분하지만 입고처리 가능
    @DisplayName("2개 이상의 부품 요청 모두가 '재고 불충분'이지만, 모두 '입고 기간과 창고 용량이 충분'하여 정상 승인된다.")
    @Test
    void success_verify_multiple_orders_second_case() {
        // given
        LocalDate orderDate = LocalDate.now();
        LocalDate dueDate = orderDate.plusDays(6);
        long orderRequestTotal = 120;
        List<OrderPartRequest> orderParts = List.of(
                new OrderPartRequest("1", 60),
                new OrderPartRequest("2", 60)
        );

        VerifyOrderRequest verifyOrderRequest = createVerifyOrderRequest(1L, orderDate, dueDate, orderRequestTotal, orderParts);

        PartResponseDto expectedPartResponseDto = createPartResponseDto(
                500, 1000, 100, List.of(
                        new PartInfoDto("1", 50, 3),
                        new PartInfoDto("2", 50, 5)
                )
        );
        SupplyResponseDto expectedSupplyResponseDto = createSupplyResponseDto(
                50, List.of(
                        new SupplyInfoDto("1", 25),
                        new SupplyInfoDto("2", 25)
                )
        );
        PurchaseResponseDto expectedPurchaseResponseDto = createPurchaseResponseDto(
                40, List.of(
                        new PurchaseInfoDto("1", 20),
                        new PurchaseInfoDto("2", 20)
                )
        );

        // when
        when(partStockPort.getPartStocks(new PartRequestDto(List.of("1", "2"))))
                .thenReturn(expectedPartResponseDto);
        when(supplyStockPort.getSupplyStocks(new SupplyRequestDto(dueDate, List.of("1", "2"))))
                .thenReturn(expectedSupplyResponseDto);
        when(purchaseStockPort.getPurchaseStocks(new PurchaseRequestDto(dueDate, List.of("1", "2"))))
                .thenReturn(expectedPurchaseResponseDto);

        VerifyOrderResponse actualResult = verifyOrderService.verifyOrder(verifyOrderRequest);

        // then
        assertTrue(actualResult.status());
        assertEquals(VerificationMessage.SUCCESS_INVENTORY_INSUFFICIENT_BUT_PROCESSABLE.getMessage(), actualResult.message());
    }

    // ── 반려 케이스 ──
    // 단일 요청
    // 1. 재고 불충분 + 기간 내 충당 불가 (배송 기한 초과)
    @DisplayName("단일 부품 요청이 '재고 불충분'과 '기간 내에 충당 불가'로 승인 반려된다.")
    @Test
    void false_verify_single_order_first_case() {
        // given
        LocalDate orderDate = LocalDate.now();
        // deliveryDays가 3인 상황에서 dueDate를 orderDate.plusDays(1)로 설정하여 배송 기한 초과를 유도
        LocalDate dueDate = orderDate.plusDays(1);
        long orderRequestTotal = 60;
        List<OrderPartRequest> orderParts = List.of(new OrderPartRequest("1", 60));

        VerifyOrderRequest verifyOrderRequest = createVerifyOrderRequest(1L, orderDate, dueDate, orderRequestTotal, orderParts);

        PartResponseDto expectedPartResponseDto = createPartResponseDto(
                500, 600, 100, List.of(new PartInfoDto("1", 100, 3))
        );
        SupplyResponseDto expectedSupplyResponseDto = createSupplyResponseDto(
                50, List.of(new SupplyInfoDto("1", 85))
        );
        PurchaseResponseDto expectedPurchaseResponseDto = createPurchaseResponseDto(
                40, List.of(new PurchaseInfoDto("1", 40))
        );

        // when
        when(partStockPort.getPartStocks(new PartRequestDto(List.of("1"))))
                .thenReturn(expectedPartResponseDto);
        when(supplyStockPort.getSupplyStocks(new SupplyRequestDto(dueDate, List.of("1"))))
                .thenReturn(expectedSupplyResponseDto);
        when(purchaseStockPort.getPurchaseStocks(new PurchaseRequestDto(dueDate, List.of("1"))))
                .thenReturn(expectedPurchaseResponseDto);

        VerifyOrderResponse actualResult = verifyOrderService.verifyOrder(verifyOrderRequest);

        // then
        assertFalse(actualResult.status());
        assertEquals(VerificationMessage.FAILURE_DELIVERY_TIME_EXCEEDED.getMessage() + " - Part ID: 1", actualResult.message());
    }

    // 2. 재고 불충분 + 기간 내 충당 가능하지만 창고 용량 부족
    @DisplayName("단일 부품 요청이 '재고 불충분'이면서 '기간 내 충당 가능'하지만, '창고 용량 초과'로 승인 반려된다.")
    @Test
    void false_verify_single_order_second_case() {
        // given
        LocalDate orderDate = LocalDate.now();
        LocalDate dueDate = orderDate.plusDays(8);
        long orderRequestTotal = 60;
        List<OrderPartRequest> orderParts = List.of(new OrderPartRequest("1", 60));

        VerifyOrderRequest verifyOrderRequest = createVerifyOrderRequest(1L, orderDate, dueDate, orderRequestTotal, orderParts);

        PartResponseDto expectedPartResponseDto = createPartResponseDto(
                500, 520, 100, List.of(new PartInfoDto("1", 100, 3))
        );
        SupplyResponseDto expectedSupplyResponseDto = createSupplyResponseDto(
                50, List.of(new SupplyInfoDto("1", 85))
        );
        PurchaseResponseDto expectedPurchaseResponseDto = createPurchaseResponseDto(
                40, List.of(new PurchaseInfoDto("1", 40))
        );

        // when
        when(partStockPort.getPartStocks(new PartRequestDto(List.of("1"))))
                .thenReturn(expectedPartResponseDto);
        when(supplyStockPort.getSupplyStocks(new SupplyRequestDto(dueDate, List.of("1"))))
                .thenReturn(expectedSupplyResponseDto);
        when(purchaseStockPort.getPurchaseStocks(new PurchaseRequestDto(dueDate, List.of("1"))))
                .thenReturn(expectedPurchaseResponseDto);

        VerifyOrderResponse actualResult = verifyOrderService.verifyOrder(verifyOrderRequest);

        // then
        assertFalse(actualResult.status());
        assertEquals(VerificationMessage.FAILURE_INSUFFICIENT_WAREHOUSE_CAPACITY.getMessage() + " - Part ID: 1", actualResult.message());
    }

    // 다중 요청
    // 1. 모든 부품에 대해 기간 내 충당 불가 → 실패 (첫 번째 부품 기준)
    @DisplayName("2개 이상의 부품 요청이 모두 '재고 불충분'이며, 입고 기간이 부족하여 승인 반려된다 (첫 번째 부품 기준).")
    @Test
    void false_verify_multiple_orders_first_case() {
        // given
        LocalDate orderDate = LocalDate.now();
        LocalDate dueDate = orderDate.plusDays(6);
        long orderRequestTotal = 120;
        List<OrderPartRequest> orderParts = List.of(
                new OrderPartRequest("1", 60),
                new OrderPartRequest("2", 60)
        );

        VerifyOrderRequest verifyOrderRequest = createVerifyOrderRequest(1L, orderDate, dueDate, orderRequestTotal, orderParts);

        // Part "1"의 deliveryDays가 13, "2"는 11로 모두 배송 기한 초과
        PartResponseDto expectedPartResponseDto = createPartResponseDto(
                500, 1000, 100, List.of(
                        new PartInfoDto("1", 50, 13),
                        new PartInfoDto("2", 50, 11)
                )
        );
        SupplyResponseDto expectedSupplyResponseDto = createSupplyResponseDto(
                50, List.of(
                        new SupplyInfoDto("1", 25),
                        new SupplyInfoDto("2", 25)
                )
        );
        PurchaseResponseDto expectedPurchaseResponseDto = createPurchaseResponseDto(
                40, List.of(
                        new PurchaseInfoDto("1", 20),
                        new PurchaseInfoDto("2", 20)
                )
        );

        // when
        when(partStockPort.getPartStocks(new PartRequestDto(List.of("1", "2"))))
                .thenReturn(expectedPartResponseDto);
        when(supplyStockPort.getSupplyStocks(new SupplyRequestDto(dueDate, List.of("1", "2"))))
                .thenReturn(expectedSupplyResponseDto);
        when(purchaseStockPort.getPurchaseStocks(new PurchaseRequestDto(dueDate, List.of("1", "2"))))
                .thenReturn(expectedPurchaseResponseDto);

        VerifyOrderResponse actualResult = verifyOrderService.verifyOrder(verifyOrderRequest);

        // then
        assertFalse(actualResult.status());
        assertEquals(VerificationMessage.FAILURE_DELIVERY_TIME_EXCEEDED.getMessage() + " - Part ID: 1", actualResult.message());
    }

    // 2. 첫 번째 부품은 입고 가능하지만, 두 번째 부품은 입고 기간 부족 → 실패 (두 번째 부품 기준)
    @DisplayName("2개 이상의 부품 요청 중 첫 번째는 입고 가능하나, 두 번째의 입고 기간 부족으로 승인 반려된다.")
    @Test
    void false_verify_multiple_orders_second_case() {
        // given
        LocalDate orderDate = LocalDate.now();
        LocalDate dueDate = orderDate.plusDays(6);
        long orderRequestTotal = 120;
        List<OrderPartRequest> orderParts = List.of(
                new OrderPartRequest("1", 60),
                new OrderPartRequest("2", 60)
        );

        VerifyOrderRequest verifyOrderRequest = createVerifyOrderRequest(1L, orderDate, dueDate, orderRequestTotal, orderParts);

        // Part "1": deliveryDays 3 (문제 없음), "2": deliveryDays 11 (배송 기한 초과)
        PartResponseDto expectedPartResponseDto = createPartResponseDto(
                500, 1000, 100, List.of(
                        new PartInfoDto("1", 50, 3),
                        new PartInfoDto("2", 50, 11)
                )
        );
        SupplyResponseDto expectedSupplyResponseDto = createSupplyResponseDto(
                50, List.of(
                        new SupplyInfoDto("1", 25),
                        new SupplyInfoDto("2", 25)
                )
        );
        PurchaseResponseDto expectedPurchaseResponseDto = createPurchaseResponseDto(
                40, List.of(
                        new PurchaseInfoDto("1", 20),
                        new PurchaseInfoDto("2", 20)
                )
        );

        // when
        when(partStockPort.getPartStocks(new PartRequestDto(List.of("1", "2"))))
                .thenReturn(expectedPartResponseDto);
        when(supplyStockPort.getSupplyStocks(new SupplyRequestDto(dueDate, List.of("1", "2"))))
                .thenReturn(expectedSupplyResponseDto);
        when(purchaseStockPort.getPurchaseStocks(new PurchaseRequestDto(dueDate, List.of("1", "2"))))
                .thenReturn(expectedPurchaseResponseDto);

        VerifyOrderResponse actualResult = verifyOrderService.verifyOrder(verifyOrderRequest);

        // then
        assertFalse(actualResult.status());
        assertEquals(VerificationMessage.FAILURE_DELIVERY_TIME_EXCEEDED.getMessage() + " - Part ID: 2", actualResult.message());
    }

    // 3. 모든 부품이 기간 내 충당 가능하나, 일부 부품의 창고 용량 초과 → 실패 (첫 번째 부품 기준)
    @DisplayName("2개 이상의 부품 요청이 모두 입고 기간 내 충당 가능하지만, 일부 부품의 창고 용량 초과로 인해 승인 반려된다 (첫 번째 부품 기준).")
    @Test
    void false_verify_multiple_orders_third_case() {
        // given
        LocalDate orderDate = LocalDate.now();
        LocalDate dueDate = orderDate.plusDays(8);
        long orderRequestTotal = 120;
        List<OrderPartRequest> orderParts = List.of(
                new OrderPartRequest("1", 60),
                new OrderPartRequest("2", 60)
        );

        VerifyOrderRequest verifyOrderRequest = createVerifyOrderRequest(1L, orderDate, dueDate, orderRequestTotal, orderParts);

        // 두 부품 모두 배송 기간 내 충당 가능하지만, 창고의 총 용량이 부족함
        PartResponseDto expectedPartResponseDto = createPartResponseDto(
                500, 510, 100, List.of(
                        new PartInfoDto("1", 50, 3),
                        new PartInfoDto("2", 50, 4)
                )
        );
        SupplyResponseDto expectedSupplyResponseDto = createSupplyResponseDto(
                50, List.of(
                        new SupplyInfoDto("1", 25),
                        new SupplyInfoDto("2", 25)
                )
        );
        PurchaseResponseDto expectedPurchaseResponseDto = createPurchaseResponseDto(
                40, List.of(
                        new PurchaseInfoDto("1", 20),
                        new PurchaseInfoDto("2", 20)
                )
        );

        // when
        when(partStockPort.getPartStocks(new PartRequestDto(List.of("1", "2"))))
                .thenReturn(expectedPartResponseDto);
        when(supplyStockPort.getSupplyStocks(new SupplyRequestDto(dueDate, List.of("1", "2"))))
                .thenReturn(expectedSupplyResponseDto);
        when(purchaseStockPort.getPurchaseStocks(new PurchaseRequestDto(dueDate, List.of("1", "2"))))
                .thenReturn(expectedPurchaseResponseDto);

        VerifyOrderResponse actualResult = verifyOrderService.verifyOrder(verifyOrderRequest);

        // then
        assertFalse(actualResult.status());
        assertEquals(VerificationMessage.FAILURE_INSUFFICIENT_WAREHOUSE_CAPACITY.getMessage() + " - Part ID: 1", actualResult.message());
    }

    // 4. 전체 주문 후 창고 총 용량 초과 → 실패 (첫 번째 부품 기준)
    @DisplayName("2개 이상의 부품 요청이 모두 입고 기간 내 충당 가능하며 단일 주문은 용량 내지만, 전체 주문 후 창고 용량 초과로 인해 승인 반려된다 (첫 번째 부품 기준).")
    @Test
    void false_verify_multiple_orders_forth_case() {
        // given
        LocalDate orderDate = LocalDate.now();
        LocalDate dueDate = orderDate.plusDays(8);
        long orderRequestTotal = 120;
        List<OrderPartRequest> orderParts = List.of(
                new OrderPartRequest("1", 60),
                new OrderPartRequest("2", 60)
        );

        VerifyOrderRequest verifyOrderRequest = createVerifyOrderRequest(1L, orderDate, dueDate, orderRequestTotal, orderParts);

        // 단일 부품 주문은 창고 용량 내이나, 전체 주문 후 예상 창고 수용량이 초과됨
        PartResponseDto expectedPartResponseDto = createPartResponseDto(
                500, 610, 100, List.of(
                        new PartInfoDto("1", 50, 3),
                        new PartInfoDto("2", 50, 4)
                )
        );
        SupplyResponseDto expectedSupplyResponseDto = createSupplyResponseDto(
                50, List.of(
                        new SupplyInfoDto("1", 25),
                        new SupplyInfoDto("2", 25)
                )
        );
        PurchaseResponseDto expectedPurchaseResponseDto = createPurchaseResponseDto(
                40, List.of(
                        new PurchaseInfoDto("1", 20),
                        new PurchaseInfoDto("2", 20)
                )
        );

        // when
        when(partStockPort.getPartStocks(new PartRequestDto(List.of("1", "2"))))
                .thenReturn(expectedPartResponseDto);
        when(supplyStockPort.getSupplyStocks(new SupplyRequestDto(dueDate, List.of("1", "2"))))
                .thenReturn(expectedSupplyResponseDto);
        when(purchaseStockPort.getPurchaseStocks(new PurchaseRequestDto(dueDate, List.of("1", "2"))))
                .thenReturn(expectedPurchaseResponseDto);

        VerifyOrderResponse actualResult = verifyOrderService.verifyOrder(verifyOrderRequest);

        // then
        assertFalse(actualResult.status());
        assertEquals(VerificationMessage.FAILURE_INSUFFICIENT_WAREHOUSE_CAPACITY.getMessage() + " - Part ID: 1", actualResult.message());
    }

    // ── 추가: 주문 마감일이 과거인 경우 ──
    @DisplayName("주문 마감일이 현재 날짜보다 이전인 경우, 즉시 승인 반려된다.")
    @Test
    void false_dueDate_in_past() {
        // given
        LocalDate orderDate = LocalDate.now();
        LocalDate dueDate = orderDate.minusDays(1); // 마감일이 과거
        long orderRequestTotal = 50;
        List<OrderPartRequest> orderParts = List.of(new OrderPartRequest("1", 50));

        VerifyOrderRequest verifyOrderRequest = createVerifyOrderRequest(1L, orderDate, dueDate, orderRequestTotal, orderParts);

        // when
        VerifyOrderResponse actualResult = verifyOrderService.verifyOrder(verifyOrderRequest);

        // then
        assertFalse(actualResult.status());
        assertEquals(VerificationMessage.FAILURE_DUE_DATE_TIME_EXCEED.getMessage(), actualResult.message());
    }

    // ============= ✅ DTO 생성 메서드 분리 ✅ ============= //
    private VerifyOrderRequest createVerifyOrderRequest(Long orderId, LocalDate orderDate, LocalDate dueDate, long total, List<OrderPartRequest> parts) {
        return new VerifyOrderRequest(orderId, orderDate, dueDate, total, parts);
    }

    private PartResponseDto createPartResponseDto(long warehouseCapacity, long warehouseMaxCapacity, long partTotal, List<PartInfoDto> partInfos) {
        return new PartResponseDto(warehouseCapacity, warehouseMaxCapacity, partTotal, partInfos);
    }

    private SupplyResponseDto createSupplyResponseDto(long supplyTotal, List<SupplyInfoDto> supplyInfos) {
        return new SupplyResponseDto(supplyTotal, supplyInfos);
    }

    private PurchaseResponseDto createPurchaseResponseDto(long purchaseTotal, List<PurchaseInfoDto> purchaseInfos) {
        return new PurchaseResponseDto(purchaseTotal, purchaseInfos);
    }
}
