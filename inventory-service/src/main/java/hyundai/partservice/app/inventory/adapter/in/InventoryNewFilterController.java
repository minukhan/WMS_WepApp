package hyundai.partservice.app.inventory.adapter.in;

import hyundai.partservice.app.inventory.adapter.dto.InventoryFilterResponse;
import hyundai.partservice.app.inventory.application.port.in.InventoryNewFilterUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "재고 관련 API",
        description = "재고에 대한 다양한 정보를 보여준다."
)
public class InventoryNewFilterController {

    private final InventoryNewFilterUseCase inventoryNewFilterUseCase;

    @Operation(
            summary = "재고 필터 조회",
            description = """
                재고 정보를 검색 조건과 정렬 옵션을 적용하여 조회합니다.
                - 검색 유형(searchType) 및 검색어(searchText)를 지정하면 해당 조건에 맞는 재고 데이터를 조회합니다.
                - 정렬 기준(orderType)과 정렬 방식(isDesc)을 설정하여 원하는 순서로 데이터를 가져올 수 있습니다.
            """
    )
    @GetMapping("/parts/inventory/new/filter")
    public InventoryFilterResponse newFilter(
            @Parameter(description = "조회할 페이지 번호 (1부터 시작)", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "페이지당 조회할 데이터 개수", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "검색 타입 (예: '부품 코드', '부품명', '적재 위치', '납품 업체명')", example = "부품 코드")
            @RequestParam(required = false) String searchType,

            @Parameter(description = "검색어 (검색 타입과 함께 사용)", example = "ABC123")
            @RequestParam(required = false) String searchText,

            @Parameter(description = "정렬 기준 (예: '현재 수량', '안전 수량')", example = "안전 수량")
            @RequestParam(required = false) String orderType,

            @Parameter(description = "정렬 방식 ('true' = 내림차순, 'false' = 오름차순)", example = "true")
            @RequestParam(required = false) String isDesc
    ) {
        return inventoryNewFilterUseCase.filter(page, size, searchType, searchText, orderType, isDesc);
    }
}
