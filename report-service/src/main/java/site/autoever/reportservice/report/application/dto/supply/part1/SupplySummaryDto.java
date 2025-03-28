package site.autoever.reportservice.report.application.dto.supply.part1;

public record SupplySummaryDto(
        SupplyCountDto countSummary,
        SupplyApprovalDto approveSummary
) {
}
