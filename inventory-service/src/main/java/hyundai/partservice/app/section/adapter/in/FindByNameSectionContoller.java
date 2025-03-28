package hyundai.partservice.app.section.adapter.in;

import hyundai.partservice.app.section.adapter.dto.SectionFindNameResponse;
import hyundai.partservice.app.section.adapter.dto.SectionResponse;
import hyundai.partservice.app.section.application.port.in.FindByNameSectionUseCase;
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
        name = "부품 섹션 API",
        description = "부품 섹션의 다양한 정보를 조회"
)
public class FindByNameSectionContoller {

    private final FindByNameSectionUseCase findByNameSectionUseCase;

    @GetMapping("/parts/section/name")
    @Operation(
            summary = "부품 섹션 이름 조회",
            description = "주어진 섹션 이름에 해당하는 부품 섹션 정보를 반환합니다.",
            parameters = {
                    @Parameter(name = "name", description = "조회할 섹션의 이름", required = true, example = "W-2")
            }
    )
    public SectionFindNameResponse findByName(@RequestParam String name) {
        return findByNameSectionUseCase.findByName(name);
    }
}




