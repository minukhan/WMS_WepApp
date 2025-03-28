package hyundai.storageservice.app.storage_schedule.adapter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record CalendarDateResponse(
        @Schema(description = "날짜 목록", example = "[\"2025-02-01\", \"2025-02-04\"]")
        List<LocalDate> calendar
) {
}
