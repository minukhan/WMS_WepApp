package hyundai.storageservice.app.storage_schedule.adapter.dto.fein;

public record SectionFindPositionRequest(

        String partId,
        int quantity
) {
    public static SectionFindPositionRequest of(String partId, int quantity) {
        return new SectionFindPositionRequest(partId, quantity);
    }
}
