package site.autoever.reportservice.report.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.reportservice.report.adapter.in.dto.ReadReportResponse;
import site.autoever.reportservice.report.application.port.in.UpdateReportUseCase;

@Tag(name = "보고서 수정 API", description = "보고서 관련 API")
@RestController
@RequiredArgsConstructor
public class UpdateReportController {

    private final UpdateReportUseCase updateReportUseCase;

    @Operation(
            summary = "보고서 수정 상태 변경",
            description = "특정 보고서의 isModified 상태를 true로 변경합니다."
    )
    @ApiResponse(responseCode = "200", description = "성공적으로 수정 상태가 변경된 보고서 반환")
    @ApiResponse(responseCode = "404", description = "보고서를 찾을 수 없음")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/reports/{reportId}")
    public ResponseEntity<ReadReportResponse> updateReport(
            @PathVariable(value = "reportId") String reportId
    ) {
        return ResponseEntity.ok(updateReportUseCase.updateReportStatus(reportId));
    }
}
