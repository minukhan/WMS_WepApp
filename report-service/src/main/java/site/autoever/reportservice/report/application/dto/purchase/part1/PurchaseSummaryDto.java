package site.autoever.reportservice.report.application.dto.purchase.part1;

public record PurchaseSummaryDto(
        PurchaseExpenseDto expenseSummary,
        PurchaseRequestDto requestSummary
) {
}
