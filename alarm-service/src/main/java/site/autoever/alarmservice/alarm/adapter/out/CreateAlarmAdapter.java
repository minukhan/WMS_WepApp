package site.autoever.alarmservice.alarm.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;
import site.autoever.alarmservice.alarm.application.port.out.CreateAlarmPort;
import site.autoever.alarmservice.infrastructure.repository.AlarmMongoRepository;

@Component
@RequiredArgsConstructor
public class CreateAlarmAdapter implements CreateAlarmPort {

    private final AlarmMongoRepository alarmMongoRepository;

    @Override
    public Mono<Alarm> save(Alarm alarm) {
        return alarmMongoRepository.save(alarm);
    }
}
