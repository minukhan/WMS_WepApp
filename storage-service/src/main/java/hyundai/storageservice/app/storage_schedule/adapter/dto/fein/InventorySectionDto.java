package hyundai.storageservice.app.storage_schedule.adapter.dto.fein;


public record InventorySectionDto(
        Long inventoryId,
        Long sectionId,
        String partId,
        int partQuantity,
        String sectionName,
        int floor,
        int sectionQuantity

) {

}
