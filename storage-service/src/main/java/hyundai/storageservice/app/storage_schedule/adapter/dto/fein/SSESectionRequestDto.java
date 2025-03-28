package hyundai.storageservice.app.storage_schedule.adapter.dto.fein;

public record SSESectionRequestDto(
        String sectionName,
        int floor,
        boolean isAdd
) {
}
