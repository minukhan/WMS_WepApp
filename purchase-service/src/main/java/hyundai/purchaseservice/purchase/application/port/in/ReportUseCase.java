package hyundai.purchaseservice.purchase.application.port.in;

import hyundai.purchaseservice.purchase.adapter.in.dto.ReportPurchaseAutoStockSummaryResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.ReportPurchaseExpensesSummaryResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.ReportPurchaseSummaryResponse;

public interface ReportUseCase {
    ReportPurchaseSummaryResponse createSummary(Integer year, Integer month);
    ReportPurchaseAutoStockSummaryResponse createAutoStockSummary(Integer year, Integer month);
    ReportPurchaseExpensesSummaryResponse createExpensesSummary(Integer year, Integer month);
}
