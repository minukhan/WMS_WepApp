package hyundai.safeservice.app.propriety_Stock.application.port.fein;

import hyundai.safeservice.app.propriety_Stock.adapter.dto.fein.PartListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "part-service")
public interface PartFeinClient {

    @PostMapping("/parts/safety")
    public ResponseEntity<String> modifySafetyStock(
            @RequestParam String partId,
            @RequestParam int safetystock
    );

    @PostMapping("/parts/optimal")
    public ResponseEntity<String> modifyOptimalStock(
            @RequestParam String partId,
            @RequestParam int optimalStock
    );

    @GetMapping("/parts/all")
    public PartListResponse getPartList();

}
