package hyundai.partservice.app.inventory.adapter.in;


import hyundai.partservice.app.inventory.application.port.in.InventoryIncrementPartQuantityUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "재고 관련 API",
        description = "재고에 대한 다양한 정보를 보여준다."
)
@RestController
@RequiredArgsConstructor
public class InventoryIncrementPartQuantityController {

    private final InventoryIncrementPartQuantityUseCase inventoryIncrementPartQuantityUseCase;

    @Operation(
            summary = "부품 재고 증가",
            description = "지정된 섹션, 층에서 특정 부품의 재고를 1개 증가시킵니다."
    )
    @PostMapping("/parts/inventory/increment")
    public ResponseEntity<String> incrementInventory(


            @Parameter(description = "부품 ID", required = true, example = "GHP003")
            @RequestParam String partId,

            @Parameter(description = "부품이 위치한 층", required = true, example = "2")
            @RequestParam int floor,

             @Parameter(description = "부품이 위치한 섹션 이름", required = true, example = "A-2")
            @RequestParam String sectionName
    ) {
        inventoryIncrementPartQuantityUseCase.incrementPartQuantity(partId, floor, sectionName);
        return ResponseEntity.ok("부품 수량이 증가되었습니다.");
    }
}