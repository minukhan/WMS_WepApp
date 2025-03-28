package hyundai.partservice.app.section.adapter.in;

import hyundai.partservice.app.section.adapter.dto.SectionListStorageResponse;
import hyundai.partservice.app.section.application.port.in.AllSectionToStorageUseCase;
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
public class AllSectionToStorageContoller {

    private final AllSectionToStorageUseCase allSectionToStorageUseCase;

    @Operation(summary = "전체 섹션 조회", description = "저장소에 등록된 모든 섹션 정보를 조회합니다.")
    @GetMapping("/parts/section/storage/all")
    public SectionListStorageResponse allSectionToStorage() {
        return allSectionToStorageUseCase.getAllSections();
    }
}
