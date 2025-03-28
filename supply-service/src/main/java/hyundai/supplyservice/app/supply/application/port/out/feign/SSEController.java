package hyundai.supplyservice.app.supply.application.port.out.feign;

import hyundai.supplyservice.app.supply.application.port.in.schedule.SSESectionRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "sse-service")
public interface SSEController {

    @PostMapping("sse/sections")
    ResponseEntity<Void> notifyQrScanResult(@RequestBody SSESectionRequestDto requestDto);
}
