package hyundai.partservice.app.section.adapter.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record SectionListResponse(
        List<SectionDto> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {
    public static SectionListResponse from(Page<SectionDto> sectionDtoPage) {
        return new SectionListResponse(
                sectionDtoPage.getContent(),
                sectionDtoPage.getNumber(),
                sectionDtoPage.getSize(),
                sectionDtoPage.getTotalElements(),
                sectionDtoPage.getTotalPages(),
                sectionDtoPage.isLast()
        );
    }
}
