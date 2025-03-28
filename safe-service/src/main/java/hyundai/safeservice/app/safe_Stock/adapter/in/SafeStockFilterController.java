package hyundai.safeservice.app.safe_Stock.adapter.in;

import hyundai.safeservice.app.safe_Stock.adapter.dto.SafeResponse;
import hyundai.safeservice.app.safe_Stock.application.port.in.SafeStockFilterUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Tag(name = "안전 재고 필터", description = "안전 재고 데이터를 검색 및 필터링하는 API")
public class SafeStockFilterController {

    private final SafeStockFilterUseCase safeStockFilterUseCase;

    @Operation(summary = "안전 재고 필터 조회", description = "검색 조건에 따라 안전 재고 데이터를 필터링하여 조회합니다.")
    @GetMapping("/safe/filter")
    public SafeResponse filter(

            @Parameter(description = "부품 ID", example = "BHD123")
            @RequestParam String partId,

            @Parameter(description = "검색 유형 (예: 수정자)", example = "수정자")
            @RequestParam(required = false) String searchType,

            @Parameter(description = "검색어 (검색 유형에 따라 필터링할 값)", example = "민욱")
            @RequestParam(required = false) String searchText,

            @Parameter(description = "정렬 기준 (예: 수정 일시, 수량)", example = "수량")
            @RequestParam(required = false) String orderType,

            @Parameter(description = "내림차순 여부 (true = 내림차순, false = 오름차순)", example = "false")
            @RequestParam(required = false) boolean isDesc
    ) {
        return safeStockFilterUseCase.filter(partId ,searchType, searchText, orderType, isDesc);
    }
}
