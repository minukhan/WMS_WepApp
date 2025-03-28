package site.autoever.reportservice.report.application.port.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.autoever.reportservice.report.application.dto.purchase.part1.PurchaseSummaryDto;
import site.autoever.reportservice.report.application.dto.purchase.part2.PurchaseAutoStockSummaryDto;
import site.autoever.reportservice.report.application.dto.purchase.part2.PurchaseExpensesSummaryDto;

@FeignClient(name = "purchase-service", path = "/purchase")
public interface PurchaseReportPort {

    @GetMapping("/report/summary")
    PurchaseSummaryDto getPurchaseSummary(@RequestParam("year") int year, @RequestParam("month") int month);

    @GetMapping("/report/autostock")
    PurchaseAutoStockSummaryDto getPurchaseAutoStockSummary(@RequestParam("year") int year, @RequestParam("month") int month);

    @GetMapping("/report/expenses")
    PurchaseExpensesSummaryDto getPurchaseExpensesSummary(@RequestParam("year") int year, @RequestParam("month") int month);
}
