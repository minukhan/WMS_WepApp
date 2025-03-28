package hyundai.supplyservice.app.supply.adapter.in;

import hyundai.supplyservice.app.supply.application.port.in.schedule.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "주문서 일정 관련 조회")
@PreAuthorize("hasAnyAuthority('ROLE_INFRA_MANAGER','ROLE_ADMIN','ROLE_WAREHOUSE_MANAGER','ROLE_WAREHOUSE_WORKER')")
public class ScheduleController {
    private final GetScheduleUseCase getScheduleUseCase;

    @GetMapping("supply/calendar")
    @Operation(summary = "달력에서 출고일만 조회")
    public ResponseEntity<CalendarResponseDto> getCalendar(
            @Parameter(description = "연도", example = "2025") @RequestParam Integer year,
            @Parameter(description = "월", example = "2") @RequestParam Integer month
    ) {
        CalendarResponseDto responseDto = getScheduleUseCase.getCalendar(year, month);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("supply/calendar/monthly")
    @Operation(summary = "달력에서 월별 출고 일정 스케줄 전체조회")
    public ResponseEntity<MonthlySupplyScheduleResponse> getMonthlySupplySchedule(
            @Parameter(description = "연도", example = "2025") @RequestParam Integer year,
            @Parameter(description = "월", example = "2") @RequestParam Integer month
    ){
        return ResponseEntity.ok(getScheduleUseCase.getMonthlySupplySchedule(year, month));
    }



    @GetMapping("/supply/daily")
    @Operation(summary = "오늘 출고 부품 목록 및 현황/ 현장 관리자 및 근무자 오늘의 일정 진행현황 페이지")
    public ResponseEntity<DailySupplyScheduleResponse> getDailySchedule() {
        return ResponseEntity.ok(getScheduleUseCase.getDailySupplyPartSchedule());
    }

    @GetMapping("supply/daily-progress")
    @Operation(summary = "날짜별 출고 작업 진척도 %")
    public ResponseEntity<DailySupplyProgressResponseDto> getDailyProgress() {
        return ResponseEntity.ok(getScheduleUseCase.getDailyProgress());
    }

    @GetMapping("supply/daily-parts")
    @Operation(summary = "날짜별 출고 부품 리스트-partId or partName 검색, 수량 or 가격 정렬")
    public ResponseEntity<List<DailySupplyPartPriceResponseDto>> getDailyParts(

            @Parameter(description = "검색항목 / partId 또는 partName", example = "partId")
            @RequestParam(required = false) String searchType,

            @Parameter(description = "검색어", example = "GHP001")
            @RequestParam(required = false) String searchText,

            @Parameter(description = "정렬 항목 quantity 또는 price", example = "quantity")
            @RequestParam(required = false) String orderType,

            @Parameter(description = "true면 내림차순 false는 오름차순", example = "true")
            @RequestParam(required = false) boolean isDesc
    ){
        return ResponseEntity.ok(getScheduleUseCase.getDailyParts(searchType, searchText, orderType, isDesc ));
    }


    @GetMapping("supply/daily-schedule")
    @Operation(summary = "현장 근무자 오늘 출고 품목 위치 및 현황")
    public ResponseEntity<DailySectionScheduleResponseDto> getDailySectionSchedule() {
        return ResponseEntity.ok(getScheduleUseCase.getDailySectionSchedule());
    }

    @PutMapping("supply/daily/outbound")
    @Operation(summary = "출고 - qr찍으면 해당 부품 개수 감소")
    public ResponseEntity<?>processQrScanOutbound(
            @Parameter(description = "품목코드", example = "GHP001") @RequestParam String partId,
            @Parameter(description = "출고 위치", example = "A구역 2열 2층") @RequestParam String sectionName

    ){
        getScheduleUseCase.processQrScanOutbound(partId, sectionName);
        return ResponseEntity.ok("부품 출고");
    }


}
