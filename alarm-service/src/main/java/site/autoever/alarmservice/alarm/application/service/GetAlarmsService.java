package site.autoever.alarmservice.alarm.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import site.autoever.alarmservice.alarm.adapter.in.dto.AlarmResponse;
import site.autoever.alarmservice.alarm.application.port.in.GetAlarmsUseCase;
import site.autoever.alarmservice.alarm.application.port.out.GetAlarmsPort;
import site.autoever.alarmservice.infrastructure.util.UserIdResolver;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAlarmsService implements GetAlarmsUseCase {

    private final GetAlarmsPort getAlarmsPort;
    private final UserIdResolver userIdResolver;

    @Override
    public Flux<AlarmResponse> getAllUserAlarms() {
        return userIdResolver.getCurrentUserId()  // Mono<Long> 반환
                .doOnNext(userId -> log.info("userid: " + userId))
                .flatMapMany(getAlarmsPort::findAllAlarmsByUserId)
                .map(AlarmResponse::fromEntity);
    }

    @Override
    public Flux<AlarmResponse> getUnreadUserAlarms() {
        return userIdResolver.getCurrentUserId()  // Mono<Long> 반환
                .doOnNext(userId -> log.info("userid: " + userId))
                .flatMapMany(getAlarmsPort::findUnreadAlarmsByUserId)
                .map(AlarmResponse::fromEntity);
    }
}
