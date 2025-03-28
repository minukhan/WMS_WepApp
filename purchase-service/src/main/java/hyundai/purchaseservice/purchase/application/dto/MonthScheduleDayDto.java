package hyundai.purchaseservice.purchase.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record MonthScheduleDayDto(
        @Schema(description = "일", example = "2025-02-01")
        LocalDate date,
        List<DayTodoDto> daySchedule
) {
}
