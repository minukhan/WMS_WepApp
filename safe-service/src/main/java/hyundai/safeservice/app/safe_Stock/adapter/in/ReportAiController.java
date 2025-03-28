package hyundai.safeservice.app.safe_Stock.adapter.in;

import hyundai.safeservice.app.safe_Stock.adapter.dto.PartSafeListResponse;
import hyundai.safeservice.app.safe_Stock.application.port.in.ReportAiUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "안전수량 추천 API", description = "안전수량을 추천해주는 API 입니다.")
public class ReportAiController {

    private final ReportAiUseCase reportAiUseCase;

    @GetMapping("/safe/report")
    @Operation(summary = "안전수량 추천", description = "AI의 추천을 토대로 안전수량 적정수량 추천을 받는다.")
    public PartSafeListResponse getReport(

            @RequestParam int year,
            @RequestParam int month
    ) {
        return reportAiUseCase.getReport(year, month);
    }

}
