package hyundai.storageservice.app.storage_schedule.application.port.out.fein;

import hyundai.storageservice.app.storage_schedule.adapter.dto.fein.SSESectionRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sse-service")
public interface SSEController {

    @PostMapping("sse/sections")
    ResponseEntity<Void> notifyQrScanResult(@RequestBody SSESectionRequestDto requestDto);
}
