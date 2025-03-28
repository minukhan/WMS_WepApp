package hyundai.purchaseservice.purchase.adapter.in;

import hyundai.purchaseservice.common.exception.BusinessExceptionResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.*;
import hyundai.purchaseservice.purchase.application.port.in.GetScheduleUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "입고 스케줄 조회 API", description = "입고 스케줄 조회와 관련된 API입니다.")
public class GetScheduleController {
    private final GetScheduleUseCase getScheduleUseCase;

    @Operation(summary = "관리자 달력 일정 조회", description = "month 내에 입고 일정이 존재하는 날짜들을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "날짜 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CalendarDateResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/calender")
    public ResponseEntity<CalendarDateResponse> getCalenderSchedule(
            @Parameter(description = "연도", example = "2025") @RequestParam Integer year,
            @Parameter(description = "월", example = "2") @RequestParam Integer month
    ) {
        return ResponseEntity.ok(getScheduleUseCase.getCalendar(year, month));
    }

    @Operation(summary = "관리자 달력 - 일별 일정 조회", description = "month 내에 입고 일정이 존재하는 날짜들의 상세 입고 목록을 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "날짜별 일정 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MonthScheduleResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/schedule/month")
    public ResponseEntity<MonthScheduleResponse> getMonthSchedule(
            @Parameter(description = "연도", example = "2025") @RequestParam Integer year,
            @Parameter(description = "월", example = "2") @RequestParam Integer month
    ) {
        return ResponseEntity.ok(getScheduleUseCase.getMonthSchedule(year, month));
    }


    @Operation(summary = "현장 관리자 - 오늘의 일정 - 입고 일정",
            description = "현장관리자 - 오늘의 일정 페이지의 입고 일정을 조회한다. 검색과 정렬이 가능하다. 검색과 정렬은 각각 한 항목으로만 가능하다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "입고 일정 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DaySchedulePageResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/schedule/day")
    public ResponseEntity<DaySchedulePageResponse> getDayStockScheduleTable(
            @Parameter(description = "검색하려는 항목(품목 코드, 품목 명, 납품 업체, 적치 구역 중 택1). searchText 필수.", example = "품목 코드")
            @RequestParam(required = false) String searchType,

            @Parameter(description = "검색하려는 값", example = "PH-1")
            @RequestParam(required = false) String searchText,

            @Parameter(description = "정렬하려는 항목(수량, 금액 중 택1)", example = "요청 일시")
            @RequestParam(required = false) String orderType,

            @Parameter(description = "오름차순은 false(생략 가능), 내림차순은 true", example = "true")
            @RequestParam(required = false) Boolean isDesc
    ){
        return ResponseEntity.ok(getScheduleUseCase.getDayScheduleTable(searchType, searchText, orderType, isDesc));
    }

    @Operation(summary = "현장 근무자 - 오늘의 일정(입고) & 현장 관리자 - 입고 일정 진행 현황",
            description = "현장 근무자 - 오늘의 일정 페이지의 입고 일정. 현장 관리자 - 입고 일정 진행 현황 페이지. " +
                    "품목 명, 전체 수량, 작업 완료된 수량이 나온다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "입고 일정 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DayScheduleResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/schedule/day/worker")
    public ResponseEntity<DayScheduleResponse> getDaySchedule(){
        return ResponseEntity.ok(getScheduleUseCase.getDaySchedule());
    }


    @Operation(summary = "현장 근무자 - QR(품목별 입고 상황 조회)",
            description = "현장 근무자 - 입고 QR 찍기 전 입고 진행 상황 페이지에서 입고 예정 수량, 입고 완료 수량, 적치 위치를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "입고 일정 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkerDayScheduleResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/schedule/day/worker/details")
    public ResponseEntity<WorkerDayScheduleResponse> getQRDaySchedule(){
        return ResponseEntity.ok(getScheduleUseCase.getQRDaySchedule());
    }

}
