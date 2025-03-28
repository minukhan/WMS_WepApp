package hyundai.purchaseservice.purchase.adapter.in.dto;

import hyundai.purchaseservice.purchase.application.dto.DayTodoDto;

import java.util.List;

public record DaySchedulePageResponse(
        List<DayTodoDto> daySchedule
) {
}
