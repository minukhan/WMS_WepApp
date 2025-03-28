package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

public record InventorySectionDto(
        Long inventoryId,
        Long sectionId,
        String partId,
        Integer partQuantity,
        String sectionName,
        Integer floor,
        Integer sectionQuantity
) {
    public static InventorySectionDto newInventory(InventorySectionDto inventorySection, Integer partQuantity, Integer sectionQuantity) {
        return new InventorySectionDto(
                inventorySection.inventoryId(),
                inventorySection.sectionId(),
                inventorySection.partId(),
                partQuantity,
                inventorySection.sectionName(),
                inventorySection.floor(),
                sectionQuantity
        );
    }
}
