package hyundai.partservice.app.part.adapter.dto;


import hyundai.partservice.app.inventory.adapter.dto.InventoryDto;
import hyundai.partservice.app.inventory.adapter.dto.InventorySectionDto;

import java.util.List;

public record PartInventoryResponse(

        PartDto partDto,
        List<InventorySectionDto> inventoryDtoList
) {
    public static PartInventoryResponse of(PartDto partDto, List<InventorySectionDto> inventoryDtoList) {
        return new PartInventoryResponse(partDto, inventoryDtoList);
    }
}
