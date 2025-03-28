package hyundai.safeservice.app.safe_Stock.adapter.dto;

import java.time.LocalDate;

public record SafeFilterDto(
    LocalDate updateTime,
    int quantity,
    String reason,
    String updateUserName
){
    public static SafeFilterDto of(LocalDate updateTime, int quantity, String reason, String updateUserName) {
        return new SafeFilterDto(updateTime, quantity, reason, updateUserName);
    }
}
