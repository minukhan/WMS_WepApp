package hyundai.partservice.app.section.adapter.dto;

import hyundai.partservice.app.section.application.entity.Section;

public record SectionDto(
        Long sectionId,
        String sectionName,
        int quantity,
        int maxCapacity,
        int floor
) {
    public static SectionDto from(Section section) {
        return new SectionDto(
                section.getId(),
                section.getName(),
                section.getQuantity(),
                section.getMaxCapacity(),
                section.getFloor()
        );
    }

}
