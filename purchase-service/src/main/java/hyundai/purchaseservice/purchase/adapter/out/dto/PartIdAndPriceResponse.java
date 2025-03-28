package hyundai.purchaseservice.purchase.adapter.out.dto;

public record PartIdAndPriceResponse(
        String partId,
        Long totalPrice
) {
}
