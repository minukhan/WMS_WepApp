package hyundai.partservice.app.section.adapter.in;


import hyundai.partservice.app.section.adapter.dto.SectionInventoryPartResponse;
import hyundai.partservice.app.section.adapter.dto.SectionInventoryResponse;
import hyundai.partservice.app.section.application.port.in.FindBySectionIdInventoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "부품 섹션 API",
        description = "부품 섹션의 다양한 정보를 조회"
)
public class FindBySectionIdInventoryController {

    private final FindBySectionIdInventoryUseCase findBySectionIdInventoryUseCase;

    @GetMapping("/parts/section/{sectionId}/inventory")
    @Operation(
            summary = "부품 섹션 재고 조회",
            description = "주어진 섹션 ID에 해당하는 부품 섹션의 재고 정보를 반환합니다.",
            parameters = {
                    @Parameter(name = "sectionId", description = "조회할 섹션의 ID", required = true, example = "101")
            }
    )
    public SectionInventoryPartResponse findBySectionId(@PathVariable("sectionId") Long sectionId) {
        return findBySectionIdInventoryUseCase.findBySectionId(sectionId);
    }
}