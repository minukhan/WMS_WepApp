package hyundai.partservice.app.part.adapter.in;

import hyundai.partservice.app.part.application.port.in.FindByIdModifyOptimalStockUseCase;
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
public class FindByIdModifyOptimalStockController {

    private final FindByIdModifyOptimalStockUseCase findByIdModifyOptimalStockUseCase;

    @PostMapping("/parts/optimal")
    @Operation(
            summary = "부품 적정 재고 수정",
            description = "부품 ID와 새로운 적정 재고 값을 입력하여 적정 재고를 수정합니다.",
            parameters = {
                    @Parameter(name = "partId", description = "수정할 부품의 ID", required = true, example = "P-2001"),
                    @Parameter(name = "optimalStock", description = "새로운 적정 재고 값", required = true, example = "100")
            }
    )
    public ResponseEntity<String> modifyOptimalStock(
            @RequestParam String partId,
            @RequestParam int optimalStock
    ) {
        findByIdModifyOptimalStockUseCase.modifyOptimal(partId, optimalStock);
        return ResponseEntity.ok("성공적으로 적정 재고 수정을 완료했습니다.");
    }
}

