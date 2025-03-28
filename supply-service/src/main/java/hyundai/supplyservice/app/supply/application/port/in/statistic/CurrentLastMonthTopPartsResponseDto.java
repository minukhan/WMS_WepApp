package hyundai.supplyservice.app.supply.application.port.in.statistic;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CurrentLastMonthTopPartsResponseDto(
        List<PartData> parts
) {
    public record PartData(
            @Schema(example = "계기판")
            String partName,

            @Schema(example = "100", description = "현재 월 수량")
            int currentMonthQuantity,

            @Schema(example = "50", description = "전월 수량")
            int lastMonthQuantity,

            @Schema(example = "120", description = "예측 수량")
            int predictQuantity,

            @Schema(example = "50", description = "변동 수량")
            int changeQuantity,

            @Schema(example = "100.0", description = "변동률 (%)")
            double changeRate

    ) {}

}
