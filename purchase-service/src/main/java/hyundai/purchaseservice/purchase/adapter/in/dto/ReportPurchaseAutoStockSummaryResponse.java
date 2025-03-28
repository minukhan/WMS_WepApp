package hyundai.purchaseservice.purchase.adapter.in.dto;

import hyundai.purchaseservice.purchase.application.dto.ReportPurchasePartAutoStockInfoDto;

import java.util.List;

public record ReportPurchaseAutoStockSummaryResponse(
        List<ReportPurchasePartAutoStockInfoDto> parts
) {
}
