package hyundai.storageservice.app.storage_schedule.application.port.in;

import hyundai.storageservice.app.storage_schedule.adapter.dto.MonthScheduleResponse;

public interface MonthScheduleDetailUseCase {

    public abstract MonthScheduleResponse getMonthSchedule(String year, String month);

}
