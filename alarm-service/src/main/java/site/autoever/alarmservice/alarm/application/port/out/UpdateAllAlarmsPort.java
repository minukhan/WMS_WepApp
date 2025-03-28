package site.autoever.alarmservice.alarm.application.port.out;

import reactor.core.publisher.Mono;

public interface UpdateAllAlarmsPort {
    Mono<Long> markAllAsReadByUserId(Long userId);
}