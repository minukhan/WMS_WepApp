package hyundai.supplyservice.app.supply.application.port.in.schedule;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record DailySupplyScheduleResponse(
        @Schema(description = "출고 날짜", example = "2025-02-01")
        LocalDate date,
        @Schema(description = "오늘 일자의 출고 부품 목록")
        List<DailyPartQuantity> daySchedule
) {
}
