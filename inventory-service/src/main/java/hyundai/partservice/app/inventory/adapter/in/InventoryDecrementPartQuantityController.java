package hyundai.partservice.app.inventory.adapter.in;

import hyundai.partservice.app.inventory.application.port.in.InventoryDecrementPartQuantityUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "재고 관련 API",
        description = "재고에 대한 다양한 정보를 보여준다."
)
@RestController
@RequiredArgsConstructor
public class InventoryDecrementPartQuantityController  {

    private final InventoryDecrementPartQuantityUseCase inventoryDecrementPartQuantityUseCase;

    @Operation(
            summary = "부품 재고 감소",
            description = "지정된 섹션,층에서 특정 부품의 재고를 1개 감소시킵니다."
    )
    @PostMapping("/parts/inventory/decrement")
    public ResponseEntity<String> decrementInventory(
            @Parameter(description = "부품이 위치한 섹션 이름", required = true, example = "A-2")
            @RequestParam String sectionName,

            @Parameter(description = "부품이 위치한 층", required = true, example = "2")
            @RequestParam int floor,

            @Parameter(description = "부품 ID", required = true, example = "GHP003")
            @RequestParam String partId
    ) {
        inventoryDecrementPartQuantityUseCase.decrementPartQuantity(partId, sectionName, floor);
        return ResponseEntity.ok("부품 수량이 감소되었습니다.");
    }
}
