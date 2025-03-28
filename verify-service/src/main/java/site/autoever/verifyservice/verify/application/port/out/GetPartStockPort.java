package site.autoever.verifyservice.verify.application.port.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import site.autoever.verifyservice.verify.application.port.out.dto.PartRequestDto;
import site.autoever.verifyservice.verify.application.port.out.dto.PartResponseDto;

@FeignClient(name = "part-service", path = "/parts")
public interface GetPartStockPort {
    @PostMapping("/stock")
    PartResponseDto getPartStocks(@RequestBody PartRequestDto request);
}
