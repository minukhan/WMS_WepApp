package site.autoever.alarmservice.alarm.application.port.in;

import reactor.core.publisher.Flux;
import site.autoever.alarmservice.alarm.adapter.in.dto.AlarmResponse;
import site.autoever.alarmservice.alarm.adapter.in.dto.CreateRoleBasedAlarmRequest;

public interface CreateRoleBasedAlarmUseCase {
    Flux<AlarmResponse> createRoleBasedAlarms(CreateRoleBasedAlarmRequest request);
}
