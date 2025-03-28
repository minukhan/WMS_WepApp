package site.autoever.reportservice.report.application.dto.supply.part1;

public record SupplyCountDto(
        long currentTotalPartBox,
        long lastMonthTotalPartBox,
        long changeQuantity,
        double changeRate
) {
}
