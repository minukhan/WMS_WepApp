package hyundai.partservice.app.part.adapter.in;

import hyundai.partservice.app.part.adapter.dto.PartAuthenticationResponse;
import hyundai.partservice.app.part.adapter.dto.PartIdRequest;
import hyundai.partservice.app.part.application.port.in.FindByPartIdAuthenticationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "부품 API",
        description = "부품 다양한 정보를 조회"
)
public class FindByPartIdAuthenticationContoller {

    private final FindByPartIdAuthenticationUseCase findByPartIdAuthenticationUseCase;

    @Operation(
            summary = "부품 정보 및 창고 용량 조회",
            description = "주어진 부품 ID 리스트를 기반으로 부품 정보를 조회하고, 현재 창고 내 해당 부품의 재고 상태를 반환합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "조회할 부품 ID 리스트",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PartIdRequest.class),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = "부품 ID 리스트 예시",
                                    value = "{ \"partIds\": [\"BHP166\", \"BHP167\", \"BHP168\"] }"
                            )
                    )
            )
    )
    @PostMapping("/parts/stock")
    public PartAuthenticationResponse findByPartIds(@RequestBody PartIdRequest request) {
        return findByPartIdAuthenticationUseCase.findByPartIds(request);
    }
}

