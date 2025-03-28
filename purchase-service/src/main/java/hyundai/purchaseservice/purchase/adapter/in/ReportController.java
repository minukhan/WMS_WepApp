package hyundai.purchaseservice.purchase.adapter.in;

import hyundai.purchaseservice.common.exception.BusinessExceptionResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.MonthExpensesResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.ReportPurchaseAutoStockSummaryResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.ReportPurchaseExpensesSummaryResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.ReportPurchaseSummaryResponse;
import hyundai.purchaseservice.purchase.application.port.in.ReportUseCase;
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
@Tag(name = "보고서 API", description = "보고서 페이지의 통계에 관한 API입니다.")
public class ReportController {
    private final ReportUseCase reportUseCase;

    @Operation(summary = "한달 요약", description = "보고서 - 한달 요약에서 지출비와 부품 요청 횟수의 통계를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "한달 요약 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportPurchaseSummaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/report/summary")
    public ResponseEntity<ReportPurchaseSummaryResponse> createSummary(
            @Parameter(description = "연도", example = "2025") @RequestParam Integer year,
            @Parameter(description = "월", example = "2") @RequestParam Integer month
    ) {
        return ResponseEntity.ok(reportUseCase.createSummary(year, month));
    }

    @Operation(summary = "자동 발주 상위 10개", description = "보고서 - 자동 발주 상위 10개에 대한 지난 달, 이번 달 통계를 조회한다. 최대 10개. 없는 경우 빈 배열")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "자동 발주 통계 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportPurchaseAutoStockSummaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/report/autostock")
    public ResponseEntity<ReportPurchaseAutoStockSummaryResponse> createAutoStockSummary(
            @Parameter(description = "연도", example = "2025") @RequestParam Integer year,
            @Parameter(description = "월", example = "2") @RequestParam Integer month
    ) {
        return ResponseEntity.ok(reportUseCase.createAutoStockSummary(year, month));
    }

    @Operation(summary = "지출비 상위 10개", description = "보고서 - 지출비 상위 10개에 대한 지난 달, 이번 달 통계를 조회한다. 최대 10개. 없는 경우 빈 배열")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "지출비 통계 조회 성공.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportPurchaseExpensesSummaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 데이터 요청",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class)))})
    @GetMapping("/purchase/report/expenses")
    public ResponseEntity<ReportPurchaseExpensesSummaryResponse> createExpensesSummary(
            @Parameter(description = "연도", example = "2025") @RequestParam Integer year,
            @Parameter(description = "월", example = "2") @RequestParam Integer month
    ) {
        return ResponseEntity.ok(reportUseCase.createExpensesSummary(year, month));
    }
}
