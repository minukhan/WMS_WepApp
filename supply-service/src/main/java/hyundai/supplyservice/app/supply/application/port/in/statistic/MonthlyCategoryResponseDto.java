package hyundai.supplyservice.app.supply.application.port.in.statistic;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MonthlyCategoryResponseDto(
        @Schema(example = "2025")
        int year,
        @Schema(example = "3")
        int month,
        @Schema(example = "1000")
        int totalMonthlyQuantity,
        List<CategoryRatioDto> Category
) {
}
