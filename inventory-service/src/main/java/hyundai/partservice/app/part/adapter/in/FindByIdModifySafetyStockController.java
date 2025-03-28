package hyundai.partservice.app.part.adapter.in;

import hyundai.partservice.app.part.application.port.in.FindByIdModifySafetyStockUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "부품 API",
        description = "부품 다양한 정보를 조회"
)
public class FindByIdModifySafetyStockController {

    private final FindByIdModifySafetyStockUseCase findByIdModifySafetyStockUseCase;

    @PostMapping("/parts/safety")
    @Operation(
            summary = "부품 안전 재고 수정",
            description = "부품 ID와 새로운 안전 재고 값을 입력하여 안전 재고를 수정합니다.",
            parameters = {
                    @Parameter(name = "partId", description = "수정할 부품의 ID", required = true, example = "P-1001"),
                    @Parameter(name = "safetystock", description = "새로운 안전 재고 값", required = true, example = "50")
            }
    )
    public ResponseEntity<String> modifySafetyStock(
            @RequestParam String partId,
            @RequestParam int safetystock
    ) {
        findByIdModifySafetyStockUseCase.modifySafetyStock(partId, safetystock);
        return ResponseEntity.ok("안전 수량이 성공적으로 변경되었습니다.");
    }
}
