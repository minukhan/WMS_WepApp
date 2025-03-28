package site.autoever.reportservice.report.application.port.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.autoever.reportservice.infrastructure.config.FeignConfig;
import site.autoever.reportservice.report.application.dto.parts.PartInfoWithAIDtoList;

@FeignClient(name = "safe-service", path = "/safe", configuration = FeignConfig.class)
public interface SafeReportPort {

    @GetMapping("/report")
    PartInfoWithAIDtoList getSafetyInfo(@RequestParam("year") int year, @RequestParam("month") int month);
}
