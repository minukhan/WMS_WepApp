package hyundai.partservice.app.section.adapter.in;


import hyundai.partservice.app.section.adapter.dto.EmptySpaceResponse;
import hyundai.partservice.app.section.application.port.in.EmptySpaceUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "부품 섹션 API",
        description = "부품 섹션의 다양한 정보를 조회"
)
public class EmptySpaceController {

    private final EmptySpaceUseCase emptySpaceUseCase;

    @Operation(
            summary = "빈 공간 조회",
            description = "현재 창고의 전체 빈 공간을 조회합니다."
    )
    @GetMapping("/parts/space")
    public EmptySpaceResponse getEmptySpace() {
        return emptySpaceUseCase.emptySpaceResponse();
    }
}