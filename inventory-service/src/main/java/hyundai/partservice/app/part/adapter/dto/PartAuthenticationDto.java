package hyundai.partservice.app.part.adapter.dto;


import hyundai.partservice.app.part.application.entity.Part;

public record PartAuthenticationDto(
        String partId,
        int currentStock,
        int estimatedDeliveryDays
) {
    public static PartAuthenticationDto of(Part part, int currentStock){
        return new PartAuthenticationDto(
                part.getId(),
                currentStock,
                part.getDeliveryDuration()
        );
    }
}
