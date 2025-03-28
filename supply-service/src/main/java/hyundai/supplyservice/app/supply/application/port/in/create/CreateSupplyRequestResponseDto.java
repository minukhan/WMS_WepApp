package hyundai.supplyservice.app.supply.application.port.in.create;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateSupplyRequestResponseDto(
        @Schema(description = "requestId")
        Long requestId,
        @Schema(example = "요청이 성공적으로 접수되었다")
        String message,
        @Schema(description = "주문접수상태", example = "WAITING")
        String status
) {
}
