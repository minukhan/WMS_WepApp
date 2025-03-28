package site.autoever.alarmservice.infrastructure.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;

public interface AlarmMongoRepository extends ReactiveMongoRepository<Alarm, String> {

    Flux<Alarm> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    Flux<Alarm> findAllByUserIdAndIsReadFalseOrderByCreatedAtDesc(Long userId);

}
