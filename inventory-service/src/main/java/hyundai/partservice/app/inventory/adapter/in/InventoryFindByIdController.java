package hyundai.partservice.app.inventory.adapter.in;


import hyundai.partservice.app.inventory.adapter.dto.InventoryResponse;
import hyundai.partservice.app.inventory.application.port.in.InventoryFindByIdUseCase;
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
        name = "재고 관련 API",
        description = "재고에 대한 다양한 정보를 보여준다."
)
public class InventoryFindByIdController {

    private final InventoryFindByIdUseCase inventoryFindByIdUseCase;

    @Operation(
            summary = "재고 ID로 재고 조회",
            description = "재고 ID를 입력하여 해당 재고 정보를 조회합니다."
    )
    @GetMapping("/parts/inventory/{inventoryId}")
    public InventoryResponse findById(
            @PathVariable("inventoryId")
            @Parameter(description = "조회할 재고 ID", required = true, example = "420")
            Long inventoryId) {
        return inventoryFindByIdUseCase.findById(inventoryId);
    }
}