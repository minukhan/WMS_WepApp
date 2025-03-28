package site.autoever.alarmservice.alarm.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import site.autoever.alarmservice.alarm.adapter.in.dto.AlarmResponse;
import site.autoever.alarmservice.alarm.adapter.in.dto.CreateRoleBasedAlarmRequest;
import site.autoever.alarmservice.alarm.application.port.in.CreateRoleBasedAlarmUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "역할 기반 알람 생성 API", description = "특정 역할의 사용자들에게 알람을 생성하는 API")
public class CreateRoleBasedAlarmController {

    private final CreateRoleBasedAlarmUseCase createRoleBasedAlarmUseCase;

    @Operation(
            summary = "역할 기반 알람 생성",
            description = "특정 역할을 가진 사용자들에게 알람을 생성합니다. " +
                    "입력된 역할이 ROLE_ADMIN이 아닌 경우, ROLE_ADMIN 사용자도 포함됩니다."
    )
    @PostMapping("/alarms/role-based")
    public Flux<AlarmResponse> createAlarmsForRole(@RequestBody CreateRoleBasedAlarmRequest request) {
        return createRoleBasedAlarmUseCase.createRoleBasedAlarms(request);
    }
}
