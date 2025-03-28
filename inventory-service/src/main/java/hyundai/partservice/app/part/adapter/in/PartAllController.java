package hyundai.partservice.app.part.adapter.in;

import hyundai.partservice.app.part.adapter.dto.PartListResponse;
import hyundai.partservice.app.part.application.port.in.PartAllUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "부품 API",
        description = "부품 다양한 정보를 조회"
)
public class PartAllController  {

    private final PartAllUseCase partAllUseCase;

    @Operation(
            summary = "모든 부품 조회",  // ✅ API 요약
            description = "현재 등록된 모든 부품 목록을 조회합니다."  // ✅ API 상세 설명
    )
    @GetMapping("/parts/all")
    public PartListResponse getPartList(){
        return partAllUseCase.getAllParts();
    }
}
