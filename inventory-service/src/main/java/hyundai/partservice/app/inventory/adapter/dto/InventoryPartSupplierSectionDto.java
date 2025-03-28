package hyundai.partservice.app.inventory.adapter.dto;


import hyundai.partservice.app.inventory.application.entity.Inventory;

public record InventoryPartSupplierSectionDto(
        Long InventoryId,
        String partId,
        String partName,
        int quantity,
        int safetyQuantity,
        Long sectionId,
        String sectionName,
        int sectionFloor,
        String supplierName

) {
    public static InventoryPartSupplierSectionDto from(Inventory inventory) {
        return new InventoryPartSupplierSectionDto(
                inventory.getId(),
                inventory.getPart().getId(),
                inventory.getPart().getName(),
                inventory.getPartQuantity(),
                inventory.getPart().getSafetyStock(),
                inventory.getSection().getId(),
                inventory.getSection().getName(),
                inventory.getSection().getFloor(),
                inventory.getPart().getSupplier().getName()
                );
    }
}
