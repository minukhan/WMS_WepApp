package hyundai.storageservice.app.storage_schedule.adapter.dto;

import java.util.List;

public record MonthScheduleResponse(
        List<MonthScheduleDayDto> monthSchedule
) {
    public static MonthScheduleResponse from(List<MonthScheduleDayDto> monthSchedule) {
        return new MonthScheduleResponse(monthSchedule);
    }
}
