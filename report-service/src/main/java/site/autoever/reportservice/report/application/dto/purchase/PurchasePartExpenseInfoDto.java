package site.autoever.reportservice.report.application.dto.purchase;

public record PurchasePartExpenseInfoDto(
        String partName,
        long currentMonthTotalExpense,
        long lastMonthTotalExpense,
        long changeExpense,
        double changeRate
) {
}
