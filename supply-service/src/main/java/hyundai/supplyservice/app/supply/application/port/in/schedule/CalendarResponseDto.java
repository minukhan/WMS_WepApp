package hyundai.supplyservice.app.supply.application.port.in.schedule;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record CalendarResponseDto(
        @Schema(description = "날짜 목록", example = "[\"2025-03-01\", \"2025-03-20\"]")
        List<LocalDate> calendar
) {
}
