package site.autoever.reportservice.report.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.reportservice.report.adapter.in.dto.ReadReportResponse;
import site.autoever.reportservice.report.application.port.in.ReadReportUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "Reports", description = "보고서 관련 API")  // API에 대한 태그 추가
public class ReadReportController {

    private final ReadReportUseCase readReportUseCase;

    @Operation(
            summary = "월간 보고서 조회",
            description = "특정 연도와 월에 대한 월간 보고서를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 보고서를 조회했습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청 (잘못된 연도나 월)"),
                    @ApiResponse(responseCode = "404", description = "보고서를 찾을 수 없습니다.")
            }
    )
    @GetMapping("/reports")
    public ResponseEntity<ReadReportResponse> readReport(
            @Parameter(description = "조회할 연도", required = true) @RequestParam(value = "year") int year,
            @Parameter(description = "조회할 월", required = true) @RequestParam(value = "month") int month
    ) {
        return ResponseEntity.ok(readReportUseCase.readReport(year, month));
    }

    @GetMapping("/reports/{reportId}")
    public ResponseEntity<ReadReportResponse> readReportById(
            @PathVariable(value = "reportId") String reportId
    ) {
        return ResponseEntity.ok(readReportUseCase.readReportById(reportId));
    }
}
