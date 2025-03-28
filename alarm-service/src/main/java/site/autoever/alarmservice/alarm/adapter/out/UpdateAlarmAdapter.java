package site.autoever.alarmservice.alarm.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;
import site.autoever.alarmservice.alarm.application.port.out.UpdateAlarmPort;
import site.autoever.alarmservice.infrastructure.repository.AlarmMongoRepository;

@Component
@RequiredArgsConstructor
public class UpdateAlarmAdapter implements UpdateAlarmPort {

    private final AlarmMongoRepository repository;

    @Override
    public Mono<Alarm> updateAlarm(Alarm alarm) {
        return repository.save(alarm);
    }
}
