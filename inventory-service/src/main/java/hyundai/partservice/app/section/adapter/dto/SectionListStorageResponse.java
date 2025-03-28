package hyundai.partservice.app.section.adapter.dto;

import java.util.List;

public record SectionListStorageResponse(
        List<SectionDto> sectionDtos
) {
    public static SectionListStorageResponse from(List<SectionDto> sectionDtos) {

        return new SectionListStorageResponse(sectionDtos);
    }
}
