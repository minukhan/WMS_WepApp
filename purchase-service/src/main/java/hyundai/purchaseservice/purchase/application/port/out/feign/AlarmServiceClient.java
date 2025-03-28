package hyundai.purchaseservice.purchase.application.port.out.feign;

import hyundai.purchaseservice.purchase.adapter.out.dto.feign.AlarmRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "alarm-service")
public interface AlarmServiceClient {
    @PostMapping("/alarms/role-based")
    void alarm(@RequestBody AlarmRequest alarmRequest);
}
