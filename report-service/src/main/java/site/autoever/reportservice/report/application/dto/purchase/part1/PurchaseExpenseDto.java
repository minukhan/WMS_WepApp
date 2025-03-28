package site.autoever.reportservice.report.application.dto.purchase.part1;

public record PurchaseExpenseDto(
        long currentTotalExpense,
        long lastMonthTotalExpense,
        long changeQuantity,
        double changeRate
) {
}
