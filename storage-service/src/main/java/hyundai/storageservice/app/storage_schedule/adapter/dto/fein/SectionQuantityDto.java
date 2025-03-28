package hyundai.storageservice.app.storage_schedule.adapter.dto.fein;


public record SectionQuantityDto(
        Long sectionId,
        String sectionName,
        int quantity,
        int maxCapacity,
        int floor,
        int allocatedAmount
) {

}
