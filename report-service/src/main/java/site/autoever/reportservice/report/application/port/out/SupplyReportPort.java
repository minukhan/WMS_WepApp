package site.autoever.reportservice.report.application.port.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.autoever.reportservice.report.application.dto.supply.part1.SupplySummaryDto;
import site.autoever.reportservice.report.application.dto.supply.part2.SupplyQuantitySummaryDto;

@FeignClient(name = "supply-service", path = "/supply")
public interface SupplyReportPort {

    @GetMapping("/monthly/total-count")
    SupplySummaryDto getSupplySummary(@RequestParam("year") int year, @RequestParam("month") int month);

    @GetMapping("/monthly/part-trends")
    SupplyQuantitySummaryDto getSupplyQuantitySummary(@RequestParam("year") int year, @RequestParam("month") int month);
}
