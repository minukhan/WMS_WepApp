package hyundai.purchaseservice.purchase.adapter.in;

import hyundai.purchaseservice.common.exception.BusinessExceptionResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.*;
import hyundai.purchaseservice.purchase.application.port.in.PurchaseStatisticsUseCase;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "통계 조회 API", description = "통계 및 그래프와 관련된 API입니다.")
public class PurchaseStatisticsController {
    private final PurchaseStatisticsUseCase purchaseStatisticsUseCase;

    @Operation(summary = "입고 작업 진행 현황", description = "입고할 품목의 전체 개수, 입고 완료된 개수, 퍼센트를 조회할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "퍼센트 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProgressChartResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/progress/chart")
    public ResponseEntity<ProgressChartResponse> getProgressChart() {
        return ResponseEntity.ok(purchaseStatisticsUseCase.getWorkingProgress());
    }


    @Operation(summary = "부품별 지출비 - 카테고리 별", description = "이번 달에 입고된 부품의 지출비를 카테고리 별로 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 별 지출비 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/category/expenses")
    public ResponseEntity<ExpensesResponse> getExpensesByCategory() {
        return ResponseEntity.ok(purchaseStatisticsUseCase.getExpensesByCategory());
    }

    @Operation(summary = "부품별 지출비 - 부품 별", description = "카테고리를 클릭했을 때 부품 별로 이번 달 지출비를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부품 별 지출비 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpensesResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/{categoryName}/part/expenses")
    public ResponseEntity<ExpensesResponse> getExpensesByPart(
            @Parameter(description = "카테고리 이름", example = "실린더") @PathVariable String categoryName
    ){
        return ResponseEntity.ok(purchaseStatisticsUseCase.getExpensesByPart(categoryName));
    }


    @Operation(summary = "부품별 빈도수 - 카테고리 별", description = "이번 달에 입고된 부품의 빈도 수를 카테고리 별로 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 별 빈도 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FrequencyResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/category/frequency")
    public ResponseEntity<FrequencyResponse> getFrequencyByCategory() {
        return ResponseEntity.ok(purchaseStatisticsUseCase.getFrequencyByCategory());
    }

    @Operation(summary = "부품별 빈도수 - 부품 별", description = "카테고리를 클릭했을 때 부품 별로 이번 달 빈도 수를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "부품 별 빈도 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FrequencyResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/{categoryName}/part/frequency")
    public ResponseEntity<FrequencyResponse> getFrequencyByPart(
            @Parameter(description = "카테고리 이름", example = "실린더") @PathVariable String categoryName
    ){
        return ResponseEntity.ok(purchaseStatisticsUseCase.getFrequencyByPart(categoryName));
    }


    /*월별 지출비 통계*/
    @Operation(summary = "월별 지출비", description = "이번 달을 기준으로 지난 1년간 지출비를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "월별 지출비 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MonthExpensesResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/month/expenses")
    public ResponseEntity<MonthExpensesResponse> getMonthlyExpenses(){
        return ResponseEntity.ok(purchaseStatisticsUseCase.getMonthExpenses());
    }
}
