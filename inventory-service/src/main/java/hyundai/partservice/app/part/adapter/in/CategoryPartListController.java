package hyundai.partservice.app.part.adapter.in;


import hyundai.partservice.app.part.adapter.dto.PartListPaginationResponse;
import hyundai.partservice.app.part.application.port.in.CategoryPartListUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "부품 API",
        description = "부품 다양한 정보를 조회"
)
public class CategoryPartListController {

    private final CategoryPartListUseCase categoryPartListUseCase;

    @Operation(
            summary = "카테고리별 부품 조회",
            description = "입력된 카테고리에 해당하는 부품 목록을 페이지네이션을 적용하여 조회합니다.",
            parameters = {
                    @Parameter(name = "category", description = "조회할 부품 카테고리", required = true, example = "엔진"),
                    @Parameter(name = "page", description = "조회할 페이지 번호 (0부터 시작)", example = "0", required = false),
                    @Parameter(name = "size", description = "한 페이지당 조회할 부품 개수", example = "10", required = false),
                    @Parameter(name = "sort", description = "정렬 기준 (예: name,desc)", example = "name,asc", required = false)
            }
    )
    @GetMapping("/parts/category")
    public PartListPaginationResponse getCategoryPartList(
            @RequestParam String category,
            Pageable pageable
    ) {
        return categoryPartListUseCase.getPartList(category, pageable);
    }
}