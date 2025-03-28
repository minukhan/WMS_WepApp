package site.autoever.alarmservice.alarm.application.port.out;

import reactor.core.publisher.Flux;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;

public interface GetAlarmsPort {
    Flux<Alarm> findAllAlarmsByUserId(Long userId);
    Flux<Alarm> findUnreadAlarmsByUserId(Long userId);
}
