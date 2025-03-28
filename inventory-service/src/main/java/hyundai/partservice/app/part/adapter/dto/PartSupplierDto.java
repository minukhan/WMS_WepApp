package hyundai.partservice.app.part.adapter.dto;

import hyundai.partservice.app.part.application.entity.Part;

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
    public static PartSupplierDto from(Part part) {
        return new PartSupplierDto(
                part.getId(),
                part.getName(),
                part.getQuantity(),
                part.getSafetyStock(),
                part.getMaxStock(),
                part.getOptimalStock(),
                part.getDeliveryDuration(),
                part.getPrice(),
                part.getCategory(),
                part.getSupplier().getName(),
                part.getSupplier().getAddress(),
                part.getSupplier().getPhoneNumber()
        );
    }
}
