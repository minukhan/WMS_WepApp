package site.autoever.alarmservice.alarm.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;
import site.autoever.alarmservice.alarm.application.port.out.GetAlarmsPort;
import site.autoever.alarmservice.infrastructure.repository.AlarmMongoRepository;

@Component
@RequiredArgsConstructor
public class GetAlarmsAdapter implements GetAlarmsPort {

    private final AlarmMongoRepository repository;

    @Override
    public Flux<Alarm> findAllAlarmsByUserId(Long userId) {
        return repository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public Flux<Alarm> findUnreadAlarmsByUserId(Long userId) {
        return repository.findAllByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }
}
