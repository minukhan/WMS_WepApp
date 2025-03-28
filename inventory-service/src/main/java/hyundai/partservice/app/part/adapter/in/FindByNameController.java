package hyundai.partservice.app.part.adapter.in;


import hyundai.partservice.app.part.adapter.dto.PartResponse;
import hyundai.partservice.app.part.application.port.in.FindByNamePartUseCase;
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
        name = "부품 API",
        description = "부품 다양한 정보를 조회"
)
public class FindByNameController {
    private final FindByNamePartUseCase findByNamePartUseCase;

    @Operation(
            summary = "부품 이름으로 조회",
            description = "입력된 부품 이름을 기반으로 해당하는 부품 정보를 반환합니다.",
            parameters = {
                    @Parameter(name = "name", description = "조회할 부품의 이름", required = true, example = "배터리")
            }
    )
    @GetMapping("/parts/name")
    public PartResponse findPartByName(@RequestParam String name) {
        return findByNamePartUseCase.findByNamePart(name);
    }
}
