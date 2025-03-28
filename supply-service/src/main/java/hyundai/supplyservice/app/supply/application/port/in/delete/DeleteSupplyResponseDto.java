package hyundai.supplyservice.app.supply.application.port.in.delete;

import io.swagger.v3.oas.annotations.media.Schema;

public record DeleteSupplyResponseDto(
        @Schema(description = "삭제요청 requestId",example = "1")
        Long requestId,
        @Schema(description = "요청 상태, waiting아니면 삭제 불가", example = "waiting")
        String status,
        @Schema(example = "요청이 성공적으로 삭제되었습니다")
        String message

) {
}
