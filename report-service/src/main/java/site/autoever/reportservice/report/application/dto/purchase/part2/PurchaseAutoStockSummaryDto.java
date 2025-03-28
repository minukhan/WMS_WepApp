package site.autoever.reportservice.report.application.dto.purchase.part2;

import site.autoever.reportservice.report.application.dto.purchase.PurchasePartAutoStockInfoDto;

import java.util.List;

public record PurchaseAutoStockSummaryDto(
        List<PurchasePartAutoStockInfoDto> parts
) {
}
