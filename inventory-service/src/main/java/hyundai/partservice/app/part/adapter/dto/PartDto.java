package hyundai.partservice.app.part.adapter.dto;


import hyundai.partservice.app.part.application.entity.Part;

public record PartDto(
        String partId,
        String partName,
        int quantity,
        int safetyStock,
        int maxStock,
        int optimalStock,
        int deliveryDuration,
        Long price,
        String category

) {

    public static PartDto from(Part part) {
        return new PartDto(
                part.getId(),
                part.getName(),
                part.getQuantity(),
                part.getSafetyStock(),
                part.getMaxStock(),
                part.getOptimalStock(),
                part.getDeliveryDuration(),
                part.getPrice(),
                part.getCategory()
        );
    }
}
