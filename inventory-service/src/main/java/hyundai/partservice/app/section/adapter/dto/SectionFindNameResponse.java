package hyundai.partservice.app.section.adapter.dto;

import hyundai.partservice.app.section.application.entity.Section;

import java.util.List;

public record SectionFindNameResponse(
        List<SectionDto> sectionDtos
) {
    public static SectionFindNameResponse from(List<SectionDto> sectionDtos) {
        return new SectionFindNameResponse(sectionDtos);
    }
}
