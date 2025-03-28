package hyundai.partservice.app.section.adapter.dto;

import java.util.List;

public record SectionPartInventoryResponse(
        List<SectionPartInventoryDto> sectionPartInventoryDtos
) {
    public static SectionPartInventoryResponse from(List<SectionPartInventoryDto> sectionPartInventoryDtos) {
        return new SectionPartInventoryResponse(sectionPartInventoryDtos);
    }
}
