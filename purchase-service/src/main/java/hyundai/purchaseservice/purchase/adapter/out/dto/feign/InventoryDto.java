package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

public record InventoryDto (
        Long inventoryId,
        Long sectionId,
        String partId,
        Integer partQuantity
){
}
