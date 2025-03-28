package site.autoever.alarmservice.alarm.adapter.in.dto;

import site.autoever.alarmservice.alarm.application.domain.model.Alarm;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public record AlarmResponse(
        String id,
        long userId,
        String message,
        String type,
        boolean isRead,
        String createdAt // KST 변환 후 String으로 저장
) {
    public static AlarmResponse fromEntity(Alarm alarm) {
        // UTC 시간을 한국 시간(KST)으로 변환
        ZonedDateTime koreaTime = alarm.getCreatedAt().atZone(ZoneId.of("Asia/Seoul"));

        // 한국 시간으로 변환된 날짜를 ISO-8601 형식의 문자열로 반환
        String formattedCreatedAt = koreaTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        return new AlarmResponse(
                alarm.getId(),
                alarm.getUserId(),
                alarm.getMessage(),
                alarm.getType(),
                alarm.isRead(),
                formattedCreatedAt
        );
    }
}
