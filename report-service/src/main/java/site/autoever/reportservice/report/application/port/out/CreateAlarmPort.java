package site.autoever.reportservice.report.application.port.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import site.autoever.reportservice.report.application.dto.alarm.AlarmCreateRequest;

@FeignClient(name = "alarm-service", path = "/alarms")
public interface CreateAlarmPort {
    @PostMapping("/role-based")
    void createAlarm(@RequestBody AlarmCreateRequest request);
}
