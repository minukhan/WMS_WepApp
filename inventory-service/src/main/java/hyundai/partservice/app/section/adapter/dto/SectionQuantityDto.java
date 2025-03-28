package hyundai.partservice.app.section.adapter.dto;

import hyundai.partservice.app.section.application.entity.Section;

public record SectionQuantityDto(
        Long sectionId,
        String sectionName,
        int quantity,
        int maxCapacity,
        int floor,
        int allocatedAmount
) {
    public static SectionQuantityDto from(Section section, int allocatedAmount) {
        return new SectionQuantityDto(
                section.getId(),
                section.getName(),
                section.getQuantity(),
                section.getMaxCapacity(),
                section.getFloor(),
                allocatedAmount
        );
    }
}
