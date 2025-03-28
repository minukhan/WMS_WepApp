package site.autoever.reportservice.report.application.dto.purchase.part1;

public record PurchaseRequestDto(
        long currentRequestOrders,
        long lastMonthRequestOrders,
        long changeQuantity,
        double changeRate
) {
}
