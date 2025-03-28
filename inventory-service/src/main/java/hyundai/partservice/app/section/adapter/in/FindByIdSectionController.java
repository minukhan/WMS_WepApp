package hyundai.partservice.app.section.adapter.in;


import hyundai.partservice.app.section.adapter.dto.SectionResponse;
import hyundai.partservice.app.section.application.port.in.FindByIdSectionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "부품 섹션 API",
        description = "부품 섹션의 다양한 정보를 조회"
)
public class FindByIdSectionController {

    private final FindByIdSectionUseCase findByIdUseCase;

    @GetMapping("/parts/section/{sectionId}")
    @Operation(
            summary = "재고 위치 상세 조회",
            description = "주어진 섹션 ID에 해당하는 재고 위치 상세 정보를 반환합니다.",
            parameters = {
                    @Parameter(name = "sectionId", description = "조회할 섹션의 ID", required = true, example = "1")
            }
    )
    public SectionResponse findById(@PathVariable Long sectionId) {
        return findByIdUseCase.findById(sectionId);
    }
}
