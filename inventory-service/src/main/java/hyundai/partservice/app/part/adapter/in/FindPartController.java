package hyundai.partservice.app.part.adapter.in;

import hyundai.partservice.app.part.adapter.dto.PartPurchaseResponse;
import hyundai.partservice.app.part.application.port.in.FindPartUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "부품 API",
        description = "부품 다양한 정보를 조회"
)
public class FindPartController {
    private final FindPartUseCase findPartUseCase;

    @Operation(
            summary = "부품 ID로 부품 정보 조회",
            description = "입력된 부품 ID에 해당하는 부품 정보를 조회합니다.",
            parameters = {
                    @Parameter(name = "partId", description = "조회할 부품의 ID", required = true, example = "DHP013")
            }
    )
    @GetMapping("/parts/{partId}")
    public PartPurchaseResponse findPart(@PathVariable String partId) {
        return findPartUseCase.findPart(partId);
    }
}