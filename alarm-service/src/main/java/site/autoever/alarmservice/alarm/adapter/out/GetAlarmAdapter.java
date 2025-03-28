package site.autoever.alarmservice.alarm.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;
import site.autoever.alarmservice.alarm.application.port.out.GetAlarmPort;
import site.autoever.alarmservice.infrastructure.repository.AlarmMongoRepository;

@Component
@RequiredArgsConstructor
public class GetAlarmAdapter implements GetAlarmPort {

    private final AlarmMongoRepository repository;

    @Override
    public Mono<Alarm> findAlarmById(String alarmId) {
        return repository.findById(alarmId);
    }
}
