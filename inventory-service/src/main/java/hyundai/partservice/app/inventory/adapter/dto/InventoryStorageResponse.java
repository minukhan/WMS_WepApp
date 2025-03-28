package hyundai.partservice.app.inventory.adapter.dto;

import java.util.List;

public record InventoryStorageResponse(

        List<InventorySectionDto> inventorySectionDtos
) {
    public static InventoryStorageResponse from(List<InventorySectionDto>  inventorySectionDtos) {
        return new InventoryStorageResponse(inventorySectionDtos);
    }
}
