package site.autoever.alarmservice.alarm.application.port.out;

import reactor.core.publisher.Mono;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;

public interface UpdateAlarmPort {

    Mono<Alarm> updateAlarm(Alarm alarm);

}
