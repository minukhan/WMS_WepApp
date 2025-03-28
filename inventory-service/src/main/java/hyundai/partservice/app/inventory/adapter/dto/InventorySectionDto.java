package hyundai.partservice.app.inventory.adapter.dto;

import hyundai.partservice.app.inventory.application.entity.Inventory;

public record InventorySectionDto(
        Long inventoryId,
        Long sectionId,
        String partId,
        int partQuantity,
        String sectionName,
        int floor,
        int sectionQuantity

) {
    public static InventorySectionDto from(Inventory inventory){
        return new InventorySectionDto(
                inventory.getId(),
                inventory.getSection().getId(),
                inventory.getPart().getId(),
                inventory.getPartQuantity(),
                inventory.getSection().getName(),
                inventory.getSection().getFloor(),
                inventory.getSection().getQuantity()
        );
    }
}
