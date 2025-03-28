package hyundai.partservice.app.inventory.adapter.in;


import hyundai.partservice.app.inventory.adapter.dto.InventoryFilterResponse;
import hyundai.partservice.app.inventory.application.port.in.InventoryFilterUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "재고 관련 API",
        description = "재고에 대한 다양한 정보를 보여준다."
)
public class InventoryFilterController {

    private final InventoryFilterUseCase inventoryFilterUseCase;

    @Operation(
            summary = "부품 재고 필터링",
            description = "입력된 조건에 따라 부품 재고 목록을 필터링합니다."
    )
    @GetMapping("/parts/inventory/filter")
    public InventoryFilterResponse inventoryFilter(

            @Parameter(description = "부품 ID", example = "BHP166")
            @RequestParam(required = false) String partId,

            @Parameter(description = "부품 이름", example = "브레이크 패드")
            @RequestParam(required = false) String partName,

            @Parameter(description = "창고 섹션 이름", example = "A구역")
            @RequestParam(required = false) String sectionName,

            @Parameter(description = "공급업체", example  = "현대모비스")
            @RequestParam(required = false) String supplier,

            @Parameter(description = "현재 수량", example = "50")
            @RequestParam(required = false) String currentQuantity,

            @Parameter(description = "안전 재고 수량", example = "10")
            @RequestParam(required = false) String safetyQuantity,

            @Parameter(hidden = true)
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        return inventoryFilterUseCase.filter(partId, partName, sectionName, supplier, currentQuantity, safetyQuantity,pageable);
    }
}
