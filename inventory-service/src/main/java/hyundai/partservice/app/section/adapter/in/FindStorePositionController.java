package hyundai.partservice.app.section.adapter.in;


import hyundai.partservice.app.section.adapter.dto.SectionFindPositionRequest;
import hyundai.partservice.app.section.adapter.dto.SectionFindPositionResponse;
import hyundai.partservice.app.section.application.port.in.FindStorePositionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "부품 API",
        description = "부품 다양한 정보를 조회"
)
public class FindStorePositionController {

    private final FindStorePositionUseCase findStorePositionUseCase;

    @PostMapping("/parts/section/store")
    @Operation(
            summary = "부품 섹션 위치 조회",
            description = "주어진 요청 데이터를 기반으로 부품 섹션의 위치를 조회합니다."
    )
    public SectionFindPositionResponse findStorePosition(
            @RequestBody SectionFindPositionRequest request
    ) {
        return findStorePositionUseCase.findStorePosition(request);
    }
}
