package hyundai.purchaseservice.purchase.adapter.in.dto;

import hyundai.purchaseservice.purchase.application.dto.MonthScheduleDayDto;

import java.util.List;

public record MonthScheduleResponse(
        List<MonthScheduleDayDto> monthSchedule
) {
}
