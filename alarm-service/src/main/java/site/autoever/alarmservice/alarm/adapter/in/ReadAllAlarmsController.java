package site.autoever.alarmservice.alarm.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import site.autoever.alarmservice.alarm.adapter.in.dto.BulkReadAlarmResponse;
import site.autoever.alarmservice.alarm.application.port.in.ReadAllAlarmsUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "알람 전체 읽기 API", description = "알람 관련 API")
public class ReadAllAlarmsController {

    private final ReadAllAlarmsUseCase readAllAlarmsUseCase;

    @Operation(summary = "모든 알람 읽음 처리", description = "현재 사용자의 모든 읽지 않은 알람을 읽음 처리합니다.")
    @PatchMapping("/alarms/read-all")
    public Mono<ResponseEntity<BulkReadAlarmResponse>> markAllAlarmsAsRead() {
        return readAllAlarmsUseCase.markAllAlarmsAsRead()
                .map(ResponseEntity::ok);
    }
}