package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

public record PartWithSupplierDto(
        String partId,
        String partName,
        Integer quantity,
        Integer safetyStock,
        Integer maxStock,
        Integer optimalStock,
        Integer deliveryDuration,
        Long price,
        String category,
        String supplierName,
        String supplierAddress,
        String supplierPhoneNumber
) {
}
