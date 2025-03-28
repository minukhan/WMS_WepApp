package hyundai.storageservice.app.storage_schedule.adapter.dto.fein;


public record ExpectedQuantityTomorrowDto(
        String partId,
        Integer quantity,
        Long sectionId
) {

}

