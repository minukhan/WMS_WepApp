package hyundai.partservice.app.part.adapter.in;


import hyundai.partservice.app.part.adapter.dto.OnlyPartNamePartIdListResponse;
import hyundai.partservice.app.part.application.port.in.OnlyNameAndPartIdAllUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "부품 API",
        description = "부품 다양한 정보를 조회"
)
public class OnlyNameAndPartIdAllController {

    private final OnlyNameAndPartIdAllUseCase onlyNameAndPartIdAllUseCase;

    @Operation(
            summary = "모든 부품의 이름과 ID 조회",
            description = "모든 부품의 이름과 부품 ID 목록을 조회합니다."
    )
    @GetMapping("/parts/onlyname")
    public OnlyPartNamePartIdListResponse onlyPartNameIdAll(){
        return onlyNameAndPartIdAllUseCase.onlyNameAndPartIdAllUseCase();
    }
}
