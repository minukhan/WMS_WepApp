package hyundai.partservice.app.part.adapter.in;


import hyundai.partservice.app.part.adapter.dto.PartListPurchaseResponse;
import hyundai.partservice.app.part.application.port.in.FindByIdListUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "부품 API",
        description = "부품 다양한 정보를 조회"
)
public class FindByIdListController {

    private final FindByIdListUseCase findByIdListUseCase;

    @Operation(
            summary = "부품 ID 목록 조회",
            description = "여러 부품 ID를 요청 본문(JSON 배열)으로 입력받아 해당 부품들의 정보를 조회합니다."
    )
    @PostMapping("/parts/partIds")
        public PartListPurchaseResponse findByIdList(
                @RequestBody
                @Parameter(description = "조회할 부품 ID 목록", required = true, example = "[\"BHP167\", \"BHP168\"]")
                List<String> partIds) {
            return findByIdListUseCase.FindByIdList(partIds);
        }
}
