package site.autoever.alarmservice.alarm.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import site.autoever.alarmservice.alarm.adapter.in.dto.AlarmResponse;
import site.autoever.alarmservice.alarm.adapter.in.dto.ReadAlarmRequest;
import site.autoever.alarmservice.alarm.application.port.in.ReadAlarmUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "알람 읽음 처리 API", description = "알람을 읽음 처리하는 API")
public class ReadAlarmController {

    private final ReadAlarmUseCase readAlarmUseCase;

    @Operation(
            summary = "알람 읽음 처리",
            description = "알람 ID를 받아 해당 알람을 읽음 상태(isRead = true)로 변경합니다."
    )
    @PatchMapping("/alarms/read")
    public Mono<ResponseEntity<AlarmResponse>> readAlarm(@RequestBody ReadAlarmRequest request) {
        return readAlarmUseCase.readAlarm(request)
                .map(ResponseEntity::ok);
    }
}
