package hyundai.supplyservice.app.supply.application.port.out.feign;

import feign.Headers;
import hyundai.supplyservice.app.supply.application.port.in.verify.VerifyOrderInfoRequestDto;
import hyundai.supplyservice.app.supply.application.port.in.verify.VerifyResultResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "verify-service")
public interface VerifyController {
    @PostMapping("/verify/order")
    VerifyResultResponseDto verifyOrder(@RequestBody VerifyOrderInfoRequestDto requestDto);

}
