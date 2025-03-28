package hyundai.partservice.app.inventory.adapter.in;

import hyundai.partservice.app.inventory.adapter.dto.InventoryListResponse;
import hyundai.partservice.app.inventory.adapter.dto.InventoryStorageResponse;
import hyundai.partservice.app.inventory.application.port.in.InventoryFindByPartIdsUseCase;
import hyundai.partservice.app.inventory.application.service.InventoryFindByPartIdsService;
import hyundai.partservice.app.part.adapter.dto.PartInventoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "재고 관련 API",
        description = "재고에 대한 다양한 정보를 보여준다."
)
public class InventoryFindByPartIdsController {

    private final InventoryFindByPartIdsUseCase inventoryFindByPartIdsUseCase;
    @Operation(
            summary = "부품 ID 목록으로 재고 정보 조회",
            description = "입력된 부품 ID 리스트에 해당하는 재고 정보를 조회합니다."
    )
    @PostMapping("/parts/inventory/info/partids")
    public InventoryStorageResponse findPartInventoryByPartIds(
            @Parameter(description = "조회할 부품 ID 리스트", required = true)
            @RequestBody List<String> partIds) {

        return inventoryFindByPartIdsUseCase.findByPartIds(partIds);
    }
}
