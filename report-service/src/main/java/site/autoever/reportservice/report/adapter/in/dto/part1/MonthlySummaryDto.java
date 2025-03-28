package site.autoever.reportservice.report.adapter.in.dto.part1;

public record MonthlySummaryDto(
        long totalShippingQuantity,
        String totalShippingQuantityMessage,
        long totalExpenses,
        String totalExpensesMessage,
        long totalPartRequestCount,
        String totalPartRequestCountMessage,
        long totalOrderRequestCount,
        long totalOrderApprovalCount,
        double approvalRate,
        String approvalRateMessage
) {
}
