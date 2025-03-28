package site.autoever.alarmservice.alarm.application.port.in;

import reactor.core.publisher.Flux;
import site.autoever.alarmservice.alarm.adapter.in.dto.AlarmResponse;

public interface GetAlarmsUseCase {

    Flux<AlarmResponse> getAllUserAlarms(); // 전체 알람
    Flux<AlarmResponse> getUnreadUserAlarms(); // 읽지 않은 알람만
}
