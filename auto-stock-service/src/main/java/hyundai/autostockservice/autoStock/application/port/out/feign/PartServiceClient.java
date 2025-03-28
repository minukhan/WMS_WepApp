package hyundai.autostockservice.autoStock.application.port.out.feign;

import hyundai.autostockservice.autoStock.adapter.dto.feign.AllPartsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "part-service")
public interface PartServiceClient {

    @GetMapping("/parts")
    AllPartsResponse getAllParts(@RequestParam(required = false) Integer page,
                                 @RequestParam(required = false) Integer size,
                                 @RequestParam(required = false) String sort);
}
