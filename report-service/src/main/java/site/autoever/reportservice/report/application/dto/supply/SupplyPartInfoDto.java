package site.autoever.reportservice.report.application.dto.supply;

public record SupplyPartInfoDto(
        String partName,
        long currentMonthQuantity,
        long lastMonthQuantity,
        long predictQuantity,
        long changeQuantity,
        double changeRate
) {
}
