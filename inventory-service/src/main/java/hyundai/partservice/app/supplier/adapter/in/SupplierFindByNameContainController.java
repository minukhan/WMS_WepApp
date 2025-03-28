package hyundai.partservice.app.supplier.adapter.in;


import hyundai.partservice.app.supplier.adapter.dto.SupplierPartResponse;
import hyundai.partservice.app.supplier.application.port.in.SupplierFindByNameContainUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "공급업체 API",
        description = "공급업체에 대한 다양한 정보"
)
public class SupplierFindByNameContainController {

    private final SupplierFindByNameContainUseCase findByNameContainUseCase;

//    @PreAuthorize("hasRole('ROLE_INFRA_MANAGER')")
    @GetMapping("/parts/supplier/contain/name")
    @Operation(
            summary = "공급업체 이름 포함 조회",
            description = "특정 문자열이 포함된 공급업체 정보를 반환합니다.",
            parameters = {
                    @Parameter(name = "name", description = "검색할 공급업체 이름에 포함될 문자열", required = true, example = "Hyundai")
            }
    )
    public SupplierPartResponse findByName(@RequestParam String name) {
        return findByNameContainUseCase.findByNameContains(name);
    }
}
