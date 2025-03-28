package site.autoever.verifyservice.verify.application.port.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import site.autoever.verifyservice.verify.application.port.out.dto.PurchaseRequestDto;
import site.autoever.verifyservice.verify.application.port.out.dto.PurchaseResponseDto;

@FeignClient(name = "purchase-service", path = "/purchase")
public interface GetPurchaseStockPort {
    @PostMapping("/stock")
    PurchaseResponseDto getPurchaseStocks(@RequestBody PurchaseRequestDto request);
}
