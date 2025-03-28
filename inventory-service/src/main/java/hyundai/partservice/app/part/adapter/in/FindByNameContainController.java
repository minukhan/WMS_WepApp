package hyundai.partservice.app.part.adapter.in;


import hyundai.partservice.app.part.adapter.dto.PartListPurchaseResponse;
import hyundai.partservice.app.part.application.port.in.FindByNameContainUseCase;
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
public class FindByNameContainController {

    private final FindByNameContainUseCase findByNameContainUseCase;

    @Operation(
            summary = "부품 이름 포함 검색",
            description = "입력된 문자열이 포함된 부품 목록을 조회합니다.",
            parameters = {
                    @Parameter(name = "name", description = "검색할 부품 이름 (부분 일치)", required = true, example = "엔진")
            }
    )
    @GetMapping("/parts/contain/name")
    public PartListPurchaseResponse findByNameContain(@RequestParam String name) {
        return findByNameContainUseCase.findByNameContains(name);
    }
}