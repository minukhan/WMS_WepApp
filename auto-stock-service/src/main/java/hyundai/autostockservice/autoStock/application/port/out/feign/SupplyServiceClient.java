package hyundai.autostockservice.autoStock.application.port.out.feign;

import hyundai.autostockservice.autoStock.adapter.dto.feign.PartIdAndQuantityDto;
import hyundai.autostockservice.autoStock.adapter.dto.feign.StockRequest;
import hyundai.autostockservice.autoStock.adapter.dto.feign.StockResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "supply-service")
public interface SupplyServiceClient {

    @PostMapping("/supply/stock")
    StockResponse getStockAmountUntilDate(@RequestBody StockRequest request);

    @PostMapping("/supply/parts/outbound-quantity")
    List<PartIdAndQuantityDto> getQuantityRequested(@RequestBody StockRequest request);
}
