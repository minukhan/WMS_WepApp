package hyundai.supplyservice.app.supply.application.port.out.feign;

import hyundai.supplyservice.app.supply.application.port.in.verify.LogCreateRequestDto;
import hyundai.supplyservice.app.supply.application.port.in.verify.VerifyOrderInfoRequestDto;
import hyundai.supplyservice.app.supply.application.port.in.verify.VerifyResultResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "log-service")
public interface LogController {
    @PostMapping("/logs/save")
    ResponseEntity<Void> createLog(@RequestBody LogCreateRequestDto requestDto);

}
