package site.autoever.reportservice.report.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.reportservice.report.adapter.in.dto.ReadReportResponse;
import site.autoever.reportservice.report.adapter.in.dto.ShowAllReportResponse;
import site.autoever.reportservice.report.application.port.in.ReadReportUseCase;
import site.autoever.reportservice.report.application.port.in.ShowAllReportUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "전체 보고서 리스트 조회", description = "보고서 관련 API")  // API에 대한 태그 추가
public class ShowAllReportController {

    private final ShowAllReportUseCase showAllReportUseCase;

    @Operation(
            summary = "전체 보고서 리스트 조회",
            description = "전체 월간 보고서를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공적으로 보고서를 조회했습니다."),
                    @ApiResponse(responseCode = "400", description = "잘못된 요청"),
                    @ApiResponse(responseCode = "404", description = "보고서를 찾을 수 없습니다.")
            }
    )
    @GetMapping("/reports/all")
    public ResponseEntity<ShowAllReportResponse> readReport() {
        return ResponseEntity.ok(showAllReportUseCase.showAll());
    }
}
