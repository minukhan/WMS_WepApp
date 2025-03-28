package hyundai.partservice.app.inventory.adapter.dto;

import hyundai.partservice.app.inventory.application.entity.Inventory;

public record InventoryDto(
        Long inventoryId,
        Long sectionId,
        String partId,
        int partQuantity
) {
    public static InventoryDto from(Inventory inventory) {
        return new InventoryDto(
                inventory.getId(),
                inventory.getSection().getId(),
                inventory.getPart().getId(),
                inventory.getPartQuantity()
        );
    }

}
