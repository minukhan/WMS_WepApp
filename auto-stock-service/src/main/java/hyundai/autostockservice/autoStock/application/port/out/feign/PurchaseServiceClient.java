package hyundai.autostockservice.autoStock.application.port.out.feign;

import hyundai.autostockservice.autoStock.adapter.dto.feign.PurchaseRequestSaveRequest;
import hyundai.autostockservice.autoStock.adapter.dto.feign.StockRequest;
import hyundai.autostockservice.autoStock.adapter.dto.feign.StockResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "purchase-service")
public interface PurchaseServiceClient {

    @PostMapping("/purchase/stock")
    StockResponse getStockAmountUntilDate(@RequestBody StockRequest request);

    @PostMapping("/purchase/save/auto")
    void saveAutoStock(@RequestBody PurchaseRequestSaveRequest request);
}
