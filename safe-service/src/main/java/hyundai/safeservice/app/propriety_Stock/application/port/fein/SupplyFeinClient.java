package hyundai.safeservice.app.propriety_Stock.application.port.fein;

import hyundai.safeservice.app.safe_Stock.adapter.dto.fein.PartCountDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "supply-service")
public interface SupplyFeinClient {

    @GetMapping("supply/parts/monthly-quantity")
    public ResponseEntity<List<PartCountDto>> getMonthlyTotalQuantityByPart(
            @Parameter(example = "2025")
            @RequestParam int year,
            @Parameter(example = "2")
            @RequestParam int month
    );


}
