package hyundai.supplyservice.app.supply.application.port.in.schedule;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MonthlySupplyScheduleResponse(
        @Schema(description = "월별 출고 스케줄 목록")
        List<DailySupplySchedulePartPriceList> monthSchedule
) {
}
