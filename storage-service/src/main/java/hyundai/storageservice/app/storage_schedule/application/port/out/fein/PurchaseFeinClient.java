package hyundai.storageservice.app.storage_schedule.application.port.out.fein;

import hyundai.storageservice.app.storage_schedule.adapter.dto.fein.ExpectedQuantityTomorrowResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "purchase-service")
public interface PurchaseFeinClient {

    @GetMapping("/purchase/stock/tomorrow")
    public ResponseEntity<ExpectedQuantityTomorrowResponse> getExpectedStockQuantityTomorrow();

}