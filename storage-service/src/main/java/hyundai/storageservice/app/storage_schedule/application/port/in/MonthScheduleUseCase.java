package hyundai.storageservice.app.storage_schedule.application.port.in;


import hyundai.storageservice.app.storage_schedule.adapter.dto.CalendarDateResponse;

public interface MonthScheduleUseCase {
    public abstract CalendarDateResponse getCalendarDate(String year, String month);
}
