package hyundai.supplyservice.app.supply.application.port.in.schedule;

import io.swagger.v3.oas.annotations.media.Schema;

public record DailySupplyPartPriceResponseDto(
        @Schema(example = "GHP001")
        String partId,
        @Schema(example = "계기판")
        String partName,
        @Schema(example = "100")
        int quantity,
        @Schema(example = "1500000")
        long totalPrice
) {
}
