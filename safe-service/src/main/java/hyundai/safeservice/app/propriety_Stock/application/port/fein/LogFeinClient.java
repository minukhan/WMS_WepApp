package hyundai.safeservice.app.propriety_Stock.application.port.fein;

import hyundai.safeservice.app.propriety_Stock.adapter.dto.fein.LogCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "log-service")
public interface LogFeinClient {
    @PostMapping("/logs/save")
    public ResponseEntity<Void> saveLog(@RequestBody LogCreateRequest request);

}
