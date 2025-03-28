package hyundai.partservice.app.section.adapter.in;


import hyundai.partservice.app.section.adapter.dto.SectionListResponse;
import hyundai.partservice.app.section.application.port.in.GetAllSectionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Tag(
        name = "부품 섹션 API",
        description = "부품 섹션의 다양한 정보를 조회"
)
public class AllSectionController {

    private final GetAllSectionUseCase allSectionUseCase;
    @GetMapping("/parts/section")
    @Operation(
            summary = "부품 섹션 목록 조회",
            description = "페이징 정보를 기준으로 부품 섹션 목록을 반환합니다.",
            parameters = {
                    @Parameter(name = "page", description = "페이지 번호 (0부터 시작)", required = false, example = "0"),
                    @Parameter(name = "size", description = "페이지 크기", required = false, example = "10"),
                    @Parameter(name = "sort", description = "정렬 기준 (예: name,asc)", required = false, example = "name,asc")
            }
    )
    public SectionListResponse GetAllSection(Pageable pageable) {
        return allSectionUseCase.allSections(pageable);

    }
}
