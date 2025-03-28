package site.autoever.reportservice.report.application.dto.purchase;

public record PurchasePartAutoStockInfoDto(
        String partName,
        long currentMonthQuantity,
        long lastMonthQuantity,
        long changeQuantity,
        double changeRate
) {
}
