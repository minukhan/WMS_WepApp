package hyundai.purchaseservice.purchase.application.port.out.feign;

import hyundai.purchaseservice.purchase.adapter.out.dto.feign.SSERequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sse-service")
public interface SSEServiceClient {
    @PostMapping("/sse/sections")
    void makeSSE(@RequestBody SSERequest request);
}
