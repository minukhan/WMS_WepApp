package site.autoever.alarmservice.alarm.application.port.in;

import reactor.core.publisher.Mono;
import site.autoever.alarmservice.alarm.adapter.in.dto.AlarmResponse;
import site.autoever.alarmservice.alarm.adapter.in.dto.ReadAlarmRequest;

public interface ReadAlarmUseCase {

    Mono<AlarmResponse> readAlarm(ReadAlarmRequest createAlarmRequest);

}
