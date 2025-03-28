package site.autoever.verifyservice.verify.application.port.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import site.autoever.verifyservice.verify.application.port.out.dto.SupplyRequestDto;
import site.autoever.verifyservice.verify.application.port.out.dto.SupplyResponseDto;

@FeignClient(name = "supply-service", path = "/supply")
public interface GetSupplyStockPort {
    @PostMapping("/stock")
    SupplyResponseDto getSupplyStocks(@RequestBody SupplyRequestDto request);
}
