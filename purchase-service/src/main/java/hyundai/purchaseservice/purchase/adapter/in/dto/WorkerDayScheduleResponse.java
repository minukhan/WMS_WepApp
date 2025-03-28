package hyundai.purchaseservice.purchase.adapter.in.dto;

import hyundai.purchaseservice.purchase.application.dto.DayScheduleDetailDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record WorkerDayScheduleResponse(
        @Schema(description = "현재 날짜", example = "2025-02-01")
        LocalDate date,
        List<DayScheduleDetailDto> daySchedule
) {
}
