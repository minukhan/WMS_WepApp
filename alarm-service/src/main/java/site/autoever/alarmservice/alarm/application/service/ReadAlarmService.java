package site.autoever.alarmservice.alarm.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import site.autoever.alarmservice.alarm.adapter.in.dto.AlarmResponse;
import site.autoever.alarmservice.alarm.adapter.in.dto.ReadAlarmRequest;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;
import site.autoever.alarmservice.alarm.application.port.in.ReadAlarmUseCase;
import site.autoever.alarmservice.alarm.application.port.out.GetAlarmPort;
import site.autoever.alarmservice.alarm.application.port.out.UpdateAlarmPort;

@Service
@RequiredArgsConstructor
public class ReadAlarmService implements ReadAlarmUseCase {

    private final GetAlarmPort getAlarmPort;
    private final UpdateAlarmPort updateAlarmPort;

    @Override
    public Mono<AlarmResponse> readAlarm(ReadAlarmRequest readAlarmRequest) {
        return getAlarmPort.findAlarmById(readAlarmRequest.alarmId())
                .flatMap(alarm -> {
                    if (alarm.isRead()) {
                        return Mono.just(alarm); // 이미 읽은 알람이면 그대로 반환
                    }
                    Alarm updatedAlarm = Alarm.builder()
                            .id(alarm.getId())
                            .userId(alarm.getUserId())
                            .message(alarm.getMessage())
                            .type(alarm.getType())
                            .isRead(true) // 읽음 처리
                            .createdAt(alarm.getCreatedAt())
                            .build();
                    return updateAlarmPort.updateAlarm(updatedAlarm);
                })
                .map(AlarmResponse::fromEntity);
    }
}
