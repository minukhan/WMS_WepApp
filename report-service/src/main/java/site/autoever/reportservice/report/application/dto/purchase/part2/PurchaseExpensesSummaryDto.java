package site.autoever.reportservice.report.application.dto.purchase.part2;

import site.autoever.reportservice.report.application.dto.purchase.PurchasePartExpenseInfoDto;

import java.util.List;

public record PurchaseExpensesSummaryDto(
        List<PurchasePartExpenseInfoDto> parts
) {
}
