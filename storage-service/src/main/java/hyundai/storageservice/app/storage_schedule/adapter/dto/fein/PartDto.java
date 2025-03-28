package hyundai.storageservice.app.storage_schedule.adapter.dto.fein;



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


}
