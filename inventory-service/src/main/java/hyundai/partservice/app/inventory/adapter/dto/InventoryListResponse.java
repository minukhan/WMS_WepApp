package hyundai.partservice.app.inventory.adapter.dto;

import java.util.List;

public record InventoryListResponse(
        List<InventoryDto> inventoryDtos
) {
    public static InventoryListResponse from(List<InventoryDto> inventoryDtos) {
        return new InventoryListResponse(inventoryDtos);
    }
}
