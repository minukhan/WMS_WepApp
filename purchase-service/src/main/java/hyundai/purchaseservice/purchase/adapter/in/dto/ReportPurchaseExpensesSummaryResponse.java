package hyundai.purchaseservice.purchase.adapter.in.dto;

import hyundai.purchaseservice.purchase.application.dto.ReportPurchasePartExpensesInfoDto;

import java.util.List;

public record ReportPurchaseExpensesSummaryResponse(
        List<ReportPurchasePartExpensesInfoDto> parts
) {
}
