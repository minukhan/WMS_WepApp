package hyundai.partservice.app.section.adapter.in;

import hyundai.partservice.app.section.adapter.dto.SectionPurchaseResponse;
import hyundai.partservice.app.section.application.port.in.FindBySectionIdListUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "부품 섹션 API",
        description = "부품 섹션의 다양한 정보를 조회"
)
public class FindBySectionIdListController {

    private final FindBySectionIdListUseCase findBySectionIdListUseCase;

    @PostMapping("/parts/sectionIds")
    @Operation(
            summary = "부품 섹션 구매 정보 조회",
            description = "주어진 섹션 ID 목록에 해당하는 부품 섹션의 구매 정보를 반환합니다."
    )
    public SectionPurchaseResponse findBySectionIdList(
            @RequestBody
            @Parameter(description = "조회할 섹션 ID 목록", required = true, example = "[101, 102, 103]")
            List<Long> sectionIds
    ) {
        return findBySectionIdListUseCase.findBySectionIdList(sectionIds);
    }
}
