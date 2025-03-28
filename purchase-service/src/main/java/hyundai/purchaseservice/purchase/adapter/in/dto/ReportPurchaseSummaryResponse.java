package hyundai.purchaseservice.purchase.adapter.in.dto;

import hyundai.purchaseservice.purchase.application.dto.ReportPurchaseRequestDto;
import hyundai.purchaseservice.purchase.application.dto.ReportPurchaseExpensesDto;

public record ReportPurchaseSummaryResponse(
        ReportPurchaseExpensesDto expenseSummary,
        ReportPurchaseRequestDto requestSummary
){
}
