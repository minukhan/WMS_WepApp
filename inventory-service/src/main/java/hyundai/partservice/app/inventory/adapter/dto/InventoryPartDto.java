package hyundai.partservice.app.inventory.adapter.dto;

import hyundai.partservice.app.inventory.application.entity.Inventory;

public record InventoryPartDto(
        Long inventoryId,
        Long sectionId,
        String partId,
        int partQuantity,
        String partName
) {
    public static InventoryPartDto from(Inventory inventory) {
        return new InventoryPartDto(
                inventory.getId(),
                inventory.getSection().getId(),
                inventory.getPart().getId(),
                inventory.getPartQuantity(),
                inventory.getPart().getName()
        );
    }

}
