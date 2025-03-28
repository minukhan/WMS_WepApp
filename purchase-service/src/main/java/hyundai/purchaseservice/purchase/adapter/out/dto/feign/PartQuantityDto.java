package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

public record PartQuantityDto(
        String partId,
        Integer quantity
) {
}
