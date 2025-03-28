package hyundai.purchaseservice.purchase.application.port.out.feign;

import hyundai.purchaseservice.purchase.adapter.out.dto.feign.ExpectedStockRequest;
import hyundai.purchaseservice.purchase.adapter.out.dto.feign.ExpectedStockResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(name = "supply-service")
public interface SupplyServiceClient {
    @GetMapping("/supply/parts/total-quantity")
    Long getTotalQuantity(@RequestParam String date);

    @PostMapping("/supply/stock")
    ExpectedStockResponse getExpectedStock(@RequestBody ExpectedStockRequest request);
}
