package site.autoever.logservice.user_log.application.dto;

import site.autoever.logservice.user_log.application.entity.UserLog;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserLogInfoDto(
        long userLogId,
        String message,
        LocalDateTime createdAt,
        long userId,
        String email,
        String name,
        String phoneNumber,
        String role
) {
    public static UserLogInfoDto from(UserLog userLog, UserInfoDto userInfoDto) {
        return new UserLogInfoDto(
                userLog.getId(),
                userLog.getMessage(),
                userLog.getCreatedAt(),
                userInfoDto.userId(),
                userInfoDto.email(),
                userInfoDto.name(),
                userInfoDto.phoneNumber(),
                userInfoDto.role()
        );
    }
}
