package site.autoever.reportservice.report.application.port.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import site.autoever.reportservice.report.application.dto.log.LogCreateRequest;

@FeignClient(name = "log-service", path = "/logs")
public interface CreateLogPort {
    @PostMapping("/save")
    void saveLog(LogCreateRequest logCreateRequest);
}
