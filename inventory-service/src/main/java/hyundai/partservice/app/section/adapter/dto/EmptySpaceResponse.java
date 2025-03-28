package hyundai.partservice.app.section.adapter.dto;



public record EmptySpaceResponse(
        int currentCount,
        int totalCount,
        int emptySpace,
        double persent
) {
    public static EmptySpaceResponse of(int currentCount, int totalCount, int emptySpace, double persent) {
        return new EmptySpaceResponse(currentCount, totalCount, emptySpace, persent);
    }
}
