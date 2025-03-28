package hyundai.supplyservice.app.supply.application.port.out.feign;

import hyundai.supplyservice.app.supply.application.port.in.commondto.AlarmCreateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "alarm-service")
public interface AlarmController {
    @PostMapping("alarms/role-based")
    ResponseEntity<Void> createAlarm(@RequestBody AlarmCreateRequestDto requestDto );
}
