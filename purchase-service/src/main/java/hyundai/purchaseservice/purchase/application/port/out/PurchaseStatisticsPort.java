package hyundai.purchaseservice.purchase.application.port.out;

import hyundai.purchaseservice.purchase.adapter.out.dto.*;

import java.util.List;

public interface PurchaseStatisticsPort {
    List<GetProgressPercentResponse> getProgressPercent();
    List<GetMonthExpensesDto> getMonthExpenses();
    List<GetDayScheduleQuantitesResponse> getDaySchedule();

    List<SearchResponse> getMonthRequest();
    List<SearchResponse> getMonthRequestByPartId(List<String> partIds);
}
