package hyundai.purchaseservice.purchase.application.port.in;

import hyundai.purchaseservice.purchase.adapter.in.dto.ExpensesResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.FrequencyResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.MonthExpensesResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.ProgressChartResponse;

public interface PurchaseStatisticsUseCase {
    ProgressChartResponse getWorkingProgress();
    ExpensesResponse getExpensesByCategory();
    ExpensesResponse getExpensesByPart(String categoryName);

    FrequencyResponse getFrequencyByCategory();
    FrequencyResponse getFrequencyByPart(String categoryName);

    MonthExpensesResponse getMonthExpenses();
}
