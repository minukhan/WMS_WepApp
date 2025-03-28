package hyundai.supplyservice.app.supply.application.port.in.verify;

import io.swagger.v3.oas.annotations.media.Schema;

public record StatusChangeResponseDto(
        @Schema(example="1")
        Long requestId,
        @Schema(example="REJECTED")
        String status,
        @Schema(example="기한 내 배송 불가 - Part ID: GHP002")
        String message
) {
}
