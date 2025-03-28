package site.autoever.alarmservice.alarm.application.port.in;

import reactor.core.publisher.Mono;
import site.autoever.alarmservice.alarm.adapter.in.dto.BulkReadAlarmResponse;

public interface ReadAllAlarmsUseCase {
    Mono<BulkReadAlarmResponse> markAllAlarmsAsRead();
}