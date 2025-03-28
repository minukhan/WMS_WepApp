package hyundai.partservice.app.section.adapter.dto;

import hyundai.partservice.app.inventory.adapter.dto.InventoryDto;
import hyundai.partservice.app.inventory.adapter.dto.InventoryPartDto;
import hyundai.partservice.app.inventory.application.entity.Inventory;

import java.util.List;

public record SectionInventoryPartResponse(

        SectionDto sectionDto,
        List<InventoryPartDto> inventories
) {
    public static SectionInventoryPartResponse of(SectionDto sectionDto, List<InventoryPartDto> inventories) {
        return new SectionInventoryPartResponse(sectionDto, inventories);
    }
}
