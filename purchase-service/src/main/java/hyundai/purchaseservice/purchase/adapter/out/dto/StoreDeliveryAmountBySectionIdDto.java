package hyundai.purchaseservice.purchase.adapter.out.dto;

public record StoreDeliveryAmountBySectionIdDto(
        Long sectionId,
        String partId,
        Integer quantity
) {
}
