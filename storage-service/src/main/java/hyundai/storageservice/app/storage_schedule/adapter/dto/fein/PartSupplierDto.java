package hyundai.storageservice.app.storage_schedule.adapter.dto.fein;


public record PartSupplierDto(
        String partId,
        String partName,
        int quantity,
        int safetyStock,
        int maxStock,
        int optimalStock,
        int deliveryDuration,
        Long price,
        String category,
        String supplierName,
        String supplierAddress,
        String supplierPhoneNumber
) {
}
