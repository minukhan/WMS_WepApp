package hyundai.partservice.app.section.adapter.in;

import hyundai.partservice.app.inventory.adapter.dto.InventoryListResponse;
import hyundai.partservice.app.section.application.port.in.FindByNameAlpaUseCase;
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
public class FindByNameAlpaController {

    private final FindByNameAlpaUseCase findByNameAlpaUseCase;

    @GetMapping("/parts/section/alpa")
    @Operation(
            summary = "알파 값으로 부품 섹션 조회",
            description = "입력된 알파 값(alpa)에 해당하는 부품 섹션 정보를 조회합니다."
    )
    public InventoryListResponse findByAlpa(
            @Parameter(
                    name = "alpa",
                    description = "조회할 알파 값",
                    required = true,
                    example = "A"
            )
            @RequestParam String alpa
    ) {
        return findByNameAlpaUseCase.findByAlpa(alpa);
    }
}
