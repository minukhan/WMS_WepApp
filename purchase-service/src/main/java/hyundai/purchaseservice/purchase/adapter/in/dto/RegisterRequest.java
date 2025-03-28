package hyundai.purchaseservice.purchase.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record RegisterRequest(
        @Schema(description = "주문자", example = "홍길동")
        String name,
        @Schema(description = "전화 번호", example = "010-1234-1234")
        String phone,
        @Schema(description = "품목 코드", example = "BHP175")
        String partId,
        @Schema(description = "주문 수량", example = "30")
        Integer quantity
) {
}
