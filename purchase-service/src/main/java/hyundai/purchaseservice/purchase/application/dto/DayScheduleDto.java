package hyundai.purchaseservice.purchase.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DayScheduleDto(
        @Schema(description = "품목 명", example = "엔진 오일")
        String partName,
        @Schema(description = "입고 전체 수량", example = "20")
        Long requestedQuantity,
        @Schema(description = "입고 완료한 개수", example = "30")
        Long currentQuantity
) {
}
