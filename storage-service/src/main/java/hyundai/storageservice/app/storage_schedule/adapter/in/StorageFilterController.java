package hyundai.storageservice.app.storage_schedule.adapter.in;

import hyundai.storageservice.app.storage_schedule.adapter.dto.FilterResponse;
import hyundai.storageservice.app.storage_schedule.application.port.in.StorageFilterUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "적치 일정 API", description = "창고 적치 일정 관련 API")
public class   StorageFilterController  {

    private final StorageFilterUseCase storageFilterUseCase;

    @Operation(summary = "적치 스케줄 필터 조회", description = """
        적치 스케줄을 다양한 조건으로 필터링합니다.
        - 검색 유형(`searchType`)과 검색어(`searchText`)를 함께 사용하여 필터링합니다.
        - 정렬 기준(`orderType`)과 내림차순 여부(`isDesc`)를 설정할 수 있습니다.
        - 모든 파라미터는 선택 사항입니다.
        """)
    @GetMapping("/storage/filter")
    public FilterResponse getStorageFilter(

            @Parameter(description = "검색 유형 (예: 품목코드, 품목명, 현재구역, 적치구역)", example = "품목코드")
            @RequestParam(required = false) String searchType,

            @Parameter(description = "검색어 (검색 유형에 따라 필터링할 값)", example = "BHP177")
            @RequestParam(required = false) String searchText,

            @Parameter(description = "정렬 기준 (예: 수량, 금액)", example = "수량")
            @RequestParam(required = false) String orderType,

            @Parameter(description = "내림차순 여부 (true = 내림차순, false = 오름차순)", example = "false")
            @RequestParam(required = false) boolean isDesc
    ) {
        return storageFilterUseCase.getFilterResponse(searchType, searchText, orderType, isDesc);
    }
}
