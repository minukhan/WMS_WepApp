package hyundai.purchaseservice.purchase.application.port.out.feign;

import hyundai.purchaseservice.purchase.adapter.out.dto.feign.LogRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "log-service")
public interface LogServiceClient {
    @PostMapping("/logs/save")
    void save(@RequestBody LogRequest log);
}
