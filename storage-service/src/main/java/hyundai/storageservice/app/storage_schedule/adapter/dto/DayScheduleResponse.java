package hyundai.storageservice.app.storage_schedule.adapter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record DayScheduleResponse(
        @Schema(description = "현재 날짜", example = "2025-02-01")
        LocalDate date,
        List<DayScheduleDto> daySchedule
) {
        public static DayScheduleResponse of(LocalDate date, List<DayScheduleDto> daySchedule) {
                return new DayScheduleResponse(date, daySchedule);
        }
}
