package site.autoever.alarmservice.alarm.application.port.in;

import reactor.core.publisher.Mono;
import site.autoever.alarmservice.alarm.adapter.in.dto.CreateAlarmRequest;
import site.autoever.alarmservice.alarm.adapter.in.dto.AlarmResponse;

public interface CreateAlarmUseCase {
    Mono<AlarmResponse> createAlarm(CreateAlarmRequest createAlarmRequest);
}
