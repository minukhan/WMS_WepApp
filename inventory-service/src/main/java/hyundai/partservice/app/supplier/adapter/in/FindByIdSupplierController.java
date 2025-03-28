package hyundai.partservice.app.supplier.adapter.in;

import hyundai.partservice.app.supplier.adapter.dto.SupplierResponse;
import hyundai.partservice.app.supplier.application.port.in.FindByIdSupplierUseCase;
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
        name = "공급업체 API",
        description = "공급업체에 대한 다양한 정보"
)
public class FindByIdSupplierController {

    private final FindByIdSupplierUseCase findByIdSupplierUseCase;
    @PreAuthorize("hasRole('ROLE_INFRA_MANAGER')")
    @GetMapping("/parts/supplier/{supplierId}")
    @Operation(
            summary = "공급업체 정보 조회",
            description = "주어진 공급업체 ID에 해당하는 공급업체 정보를 반환합니다.",
            parameters = {
                    @Parameter(name = "supplierId", description = "조회할 공급업체의 ID", required = true, example = "1001")
            }
    )
    public SupplierResponse getSupplier(@PathVariable Long supplierId) {
        return findByIdSupplierUseCase.findById(supplierId);
    }
}
