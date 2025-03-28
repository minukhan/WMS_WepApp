package site.autoever.alarmservice.alarm.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import site.autoever.alarmservice.alarm.adapter.in.dto.BulkReadAlarmResponse;
import site.autoever.alarmservice.alarm.application.port.in.ReadAllAlarmsUseCase;
import site.autoever.alarmservice.alarm.application.port.out.UpdateAllAlarmsPort;
import site.autoever.alarmservice.infrastructure.util.UserIdResolver;

@Service
@RequiredArgsConstructor
public class ReadAllAlarmsService implements ReadAllAlarmsUseCase {

    private final UserIdResolver userIdResolver;
    private final UpdateAllAlarmsPort updateAllAlarmsPort;

    @Override
    public Mono<BulkReadAlarmResponse> markAllAlarmsAsRead() {
        return userIdResolver.getCurrentUserId()
                .flatMap(updateAllAlarmsPort::markAllAsReadByUserId) // userId를 받아 업데이트 실행
                .map(BulkReadAlarmResponse::new); // 응답 DTO로 변환
    }
}
