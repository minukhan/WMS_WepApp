package site.autoever.alarmservice.alarm.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import site.autoever.alarmservice.alarm.adapter.in.dto.CreateAlarmRequest;
import site.autoever.alarmservice.alarm.adapter.in.dto.AlarmResponse;
import site.autoever.alarmservice.alarm.application.port.in.CreateAlarmUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "단일 알람 생성 API", description = "개별 사용자에게 알람을 생성하는 API")
public class CreateAlarmController {

    private final CreateAlarmUseCase createAlarmUseCase;

    @Operation(
            summary = "단일 알람 생성",
            description = "특정 사용자에게 알람을 생성합니다."
    )
    @PostMapping("/alarms")
    public Mono<ResponseEntity<AlarmResponse>> createAlarm(@RequestBody CreateAlarmRequest request) {
        return createAlarmUseCase.createAlarm(request)
                .map(ResponseEntity::ok);
    }
}
