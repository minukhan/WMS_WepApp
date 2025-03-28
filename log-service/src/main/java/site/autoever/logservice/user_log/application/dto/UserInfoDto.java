package site.autoever.logservice.user_log.application.dto;

import java.time.LocalDateTime;

public record UserInfoDto(
        long userId,
        String address,
        String email,
        String name,
        String phoneNumber,
        String role,
        LocalDateTime createdAt
) {
}
