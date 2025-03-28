package hyundai.partservice.app.section.adapter.dto;


import hyundai.partservice.app.inventory.adapter.dto.InventoryDto;

import java.util.List;

public record SectionInventoryResponse(

        SectionDto sectionDto,
        List<InventoryDto> inventories

) {
    public static SectionInventoryResponse of(SectionDto sectionDto, List<InventoryDto> inventories) {
        return new SectionInventoryResponse(sectionDto, inventories);
    }
}
