package hyundai.safeservice.app.safe_Stock.adapter.dto.fein;


public record LogCreateRequest(
        long userId,
        String message
) {
    public static LogCreateRequest of(long userId, String message) {
        return new LogCreateRequest(userId, message);
    }
}
