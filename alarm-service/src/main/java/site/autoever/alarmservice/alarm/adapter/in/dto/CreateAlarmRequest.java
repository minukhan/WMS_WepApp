package site.autoever.alarmservice.alarm.adapter.in.dto;

import site.autoever.alarmservice.alarm.application.domain.model.Alarm;

import java.time.Instant;

public record CreateAlarmRequest(
        Long userId,
        String message,
        String type
) {
    public Alarm toEntity() {
        return Alarm.builder()
                .userId(userId)
                .message(message)
                .type(type)
                .isRead(false)
                .createdAt(Instant.now())
                .build();
    }
}
