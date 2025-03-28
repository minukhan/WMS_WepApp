package site.autoever.reportservice.report.application.dto.supply.part1;

public record SupplyApprovalDto(
        long currentRequestedOrders,
        long currentApprovedOrders,
        double currentApprovalRate,
        double lastMonthApprovalRate
) {
}
