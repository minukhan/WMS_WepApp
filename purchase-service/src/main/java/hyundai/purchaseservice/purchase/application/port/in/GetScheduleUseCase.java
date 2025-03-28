package hyundai.purchaseservice.purchase.application.port.in;

import hyundai.purchaseservice.purchase.adapter.in.dto.*;

public interface GetScheduleUseCase {
    CalendarDateResponse getCalendar(Integer year, Integer month);
    MonthScheduleResponse getMonthSchedule(Integer year, Integer month);
    DaySchedulePageResponse getDayScheduleTable(String searchType, String searchText, String orderType, Boolean isDesc);
    DayScheduleResponse getDaySchedule();
    WorkerDayScheduleResponse getQRDaySchedule();
}
