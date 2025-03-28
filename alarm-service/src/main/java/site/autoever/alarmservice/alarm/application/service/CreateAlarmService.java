package site.autoever.alarmservice.alarm.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import site.autoever.alarmservice.alarm.adapter.in.dto.CreateAlarmRequest;
import site.autoever.alarmservice.alarm.adapter.in.dto.AlarmResponse;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;
import site.autoever.alarmservice.alarm.application.port.in.CreateAlarmUseCase;
import site.autoever.alarmservice.alarm.application.port.out.CreateAlarmPort;

@Service
@RequiredArgsConstructor
public class CreateAlarmService implements CreateAlarmUseCase {

    private final CreateAlarmPort createAlarmPort;

    @Override
    public Mono<AlarmResponse> createAlarm(CreateAlarmRequest createAlarmRequest) {
        Alarm alarm = createAlarmRequest.toEntity();

        return createAlarmPort.save(alarm)
                .map(AlarmResponse::fromEntity); // 저장 후 DTO 변환
    }
}
