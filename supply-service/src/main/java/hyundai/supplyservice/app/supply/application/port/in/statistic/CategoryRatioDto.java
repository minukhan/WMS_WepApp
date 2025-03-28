package hyundai.supplyservice.app.supply.application.port.in.statistic;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryRatioDto(
        @Schema(example = "엔진")
        String category,
        @Schema(example = "100")
        int totalQuantity,
        @Schema(example = "25.5")
        double ratio
) {
}
