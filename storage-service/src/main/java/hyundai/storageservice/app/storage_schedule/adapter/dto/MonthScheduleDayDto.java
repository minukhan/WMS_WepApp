package hyundai.storageservice.app.storage_schedule.adapter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record MonthScheduleDayDto(
        @Schema(description = "일", example = "2025-02-01")
        LocalDate date,
        List<DayTodoDto> daySchedule
) {
        public static MonthScheduleDayDto of(LocalDate date, List<DayTodoDto> dayTodoDtos){
                return new MonthScheduleDayDto(date, dayTodoDtos);
        }
}
