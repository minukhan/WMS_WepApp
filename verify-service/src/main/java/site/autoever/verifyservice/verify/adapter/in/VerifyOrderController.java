package site.autoever.verifyservice.verify.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.verifyservice.verify.adapter.in.dto.VerifyOrderRequest;
import site.autoever.verifyservice.verify.adapter.in.dto.VerifyOrderResponse;
import site.autoever.verifyservice.verify.application.port.in.VerifyOrderUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "주문 요청 [승인 / 반려] 여부 조회 서비스", description = "승인 / 반려 검증 API")
public class VerifyOrderController {
    private final VerifyOrderUseCase verifyOrderUseCase;

    @Operation(summary = "사용자 주문 승인/반려 여부 검증", description = "사용자 주문 정보를 바탕으로 현재 창고 잔여량, 입고 예정량, 출고 예정량, 부품 대기 일수 등을 계산하여 승인 / 반려 여부를 리턴합니다.")
    @PostMapping("/verify/order")
    VerifyOrderResponse verifyOrder(@RequestBody VerifyOrderRequest verifyOrderRequest) {
        return verifyOrderUseCase.verifyOrder(verifyOrderRequest);
    }

}
