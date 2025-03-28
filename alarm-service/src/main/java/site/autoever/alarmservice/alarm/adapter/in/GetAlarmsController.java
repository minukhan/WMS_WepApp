package site.autoever.alarmservice.alarm.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import site.autoever.alarmservice.alarm.adapter.in.dto.AlarmResponse;
import site.autoever.alarmservice.alarm.application.port.in.GetAlarmsUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "알람 조회 API", description = "알람을 조회하는 API")
public class GetAlarmsController {

    private final GetAlarmsUseCase getAlarmsUseCase;

    @Operation(
            summary = "사용자의 알람 목록 조회",
            description = "현재 로그인한 사용자의 알람 목록을 조회합니다. " +
                    "'all' 파라미터가 true이면 모든 알람을 조회하며, false(기본값)이면 읽지 않은 알람만 조회합니다."
    )
    @GetMapping("/alarms")
    public Flux<AlarmResponse> getAlarms(
            @Parameter(
                    description = "true: 모든 알람 조회, false(기본값): 읽지 않은 알람만 조회",
                    example = "false"
            )
            @RequestParam(required = false, defaultValue = "false") boolean all) {
        return all ? getAlarmsUseCase.getAllUserAlarms() : getAlarmsUseCase.getUnreadUserAlarms();
    }
}
