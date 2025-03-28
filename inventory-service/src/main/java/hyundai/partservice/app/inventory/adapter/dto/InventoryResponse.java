package hyundai.partservice.app.inventory.adapter.dto;


public record InventoryResponse(
        InventoryDto inventoryDto
) {
    public static InventoryResponse from(InventoryDto inventoryDto) {
        return new InventoryResponse(inventoryDto);
    }
}
