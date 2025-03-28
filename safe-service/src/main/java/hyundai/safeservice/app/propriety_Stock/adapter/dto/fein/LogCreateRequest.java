package hyundai.safeservice.app.propriety_Stock.adapter.dto.fein;


import java.time.LocalDateTime;
import java.time.ZoneId;

public record LogCreateRequest(
        long userId,
        String message
) {
    public static LogCreateRequest of(long userId, String message) {
        return new LogCreateRequest(userId, message);
    }
}
