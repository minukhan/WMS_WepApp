package hyundai.storageservice.app.storage_schedule.adapter.in;

import hyundai.storageservice.app.storage_schedule.adapter.dto.CalendarDateResponse;
import hyundai.storageservice.app.storage_schedule.application.port.in.MonthScheduleUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "적치 API", description = "창고 적치 일정 관련 API")
public class MonthScheduleController {

    private final MonthScheduleUseCase monthScheduleUseCase;

    @Operation(summary = "월별 창고 스케줄 조회", description = "연도와 월을 입력하면 해당 월의 창고 스케줄을 반환합니다.")
    @GetMapping("/storage/month/calendar")
    public CalendarDateResponse getMonth(
            @Parameter(description = "조회할 연도 (yyyy)", example = "2025")
            @RequestParam String year,

            @Parameter(description = "조회할 월 (MM)", example = "2")
            @RequestParam String month
    ) {
        return monthScheduleUseCase.getCalendarDate(year, month);
    }
}
