package site.autoever.logservice.user_log.adapter.in.dto;

import site.autoever.logservice.user_log.application.entity.UserLog;

import java.time.LocalDateTime;
import java.time.ZoneId;

public record LogCreateRequest(
        long userId,
        String message
) {
    public UserLog toEntity() {
        return UserLog.builder()
                .userId(userId)
                .message(message)
                .createdAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();
    }
}
