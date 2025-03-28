package hyundai.partservice.app.part.adapter.in;


import hyundai.partservice.app.part.adapter.dto.PartInventoryResponse;
import hyundai.partservice.app.part.application.port.in.FIndByPartIdInventoryListUseCase;
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
        name = "부품 API",
        description = "부품 다양한 정보를 조회"
)
public class FindByPartIdInventoryListContoller {

    private final FIndByPartIdInventoryListUseCase findByPartIdInventoryListUseCase;

    @Operation(
            summary = "부품 ID로 부품 및 재고 조회 맟 저장위치",
            description = "주어진 부품 ID에 해당하는 부품 정보를 조회하고, 해당 부품의 재고 정보 및 저장 위치까지 반환합니다.",
            parameters = {
                    @Parameter(name = "partid", description = "조회할 부품의 ID", required = true, example = "DHP013")
            }
    )
    @GetMapping("/parts/inventory/part/{partid}")
    public PartInventoryResponse getPartInventory(@PathVariable("partid") String partid) {
        return findByPartIdInventoryListUseCase.getPartInventory(partid);
    }
}
