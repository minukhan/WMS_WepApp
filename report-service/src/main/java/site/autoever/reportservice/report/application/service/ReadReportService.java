package site.autoever.reportservice.report.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.autoever.reportservice.infrastructure.util.MessageGenerator;
import site.autoever.reportservice.report.adapter.in.dto.ReadReportResponse;
import site.autoever.reportservice.report.adapter.in.dto.part1.MonthlySummaryDto;
import site.autoever.reportservice.report.adapter.in.dto.part2.*;
import site.autoever.reportservice.report.application.domain.Report;
import site.autoever.reportservice.report.application.dto.parts.PartInfoWithAIDtoList;
import site.autoever.reportservice.report.application.dto.purchase.PurchasePartAutoStockInfoDto;
import site.autoever.reportservice.report.application.dto.purchase.PurchasePartExpenseInfoDto;
import site.autoever.reportservice.report.application.dto.purchase.part1.PurchaseSummaryDto;
import site.autoever.reportservice.report.application.dto.purchase.part2.PurchaseAutoStockSummaryDto;
import site.autoever.reportservice.report.application.dto.supply.SupplyPartInfoDto;
import site.autoever.reportservice.report.application.dto.supply.part1.SupplySummaryDto;
import site.autoever.reportservice.report.application.exception.ReportNotFoundException;
import site.autoever.reportservice.report.application.port.in.ReadReportUseCase;
import site.autoever.reportservice.report.application.port.out.ReadReportPort;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadReportService implements ReadReportUseCase {

    private final ReadReportPort readReportPort;
    private final MessageGenerator messageGenerator;

    @Override
    public ReadReportResponse readReport(int year, int month) {
        Report report = readReportPort.getReport(year, month)
                .orElseThrow(ReportNotFoundException::new);

        MonthlySummaryDto monthlySummaryDto = getMonthlySummary(report);

        MonthlyStatisticsDto monthlyStatisticsDto = getMonthlyStatistics(report);

        return new ReadReportResponse(report.getId(), report.isModified(), report.getYear(), report.getMonth(), monthlySummaryDto, monthlyStatisticsDto, new PartInfoWithAIDtoList(report.getPartWithAiGraph()));
    }

    @Override
    public ReadReportResponse readReportById(String reportId) {
        Report report = readReportPort.getReportById(reportId)
                .orElseThrow(ReportNotFoundException::new);

        MonthlySummaryDto monthlySummaryDto = getMonthlySummary(report);

        MonthlyStatisticsDto monthlyStatisticsDto = getMonthlyStatistics(report);

        return new ReadReportResponse(report.getId(), report.isModified(), report.getYear(), report.getMonth(), monthlySummaryDto, monthlyStatisticsDto, new PartInfoWithAIDtoList(report.getPartWithAiGraph()));
    }

    private MonthlySummaryDto getMonthlySummary(Report report) {
        SupplySummaryDto supplySummary = report.getSupplySummary();
        PurchaseSummaryDto purchaseSummary = report.getPurchaseSummary();


        long totalShippingQuantity = supplySummary.countSummary().currentTotalPartBox();
        String totalShippingQuantityMessage = messageGenerator.getTotalShippingQuantityMessage(supplySummary.countSummary());

        long totalExpenses = purchaseSummary.expenseSummary().currentTotalExpense();
        String totalExpensesMessage = messageGenerator.getTotalExpensesMessage(purchaseSummary.expenseSummary());

        long totalPartRequestCount = purchaseSummary.requestSummary().currentRequestOrders();
        String totalPartRequestCountMessage = messageGenerator.getTotalPartRequestCountMessage(purchaseSummary.requestSummary());

        long totalOrderRequestCount = supplySummary.approveSummary().currentRequestedOrders();
        long totalOrderApprovalCount = supplySummary.approveSummary().currentApprovedOrders();
        double approvalRate = supplySummary.approveSummary().currentApprovalRate();
        String approvalRateMessage = messageGenerator.getApprovalRateMessage(supplySummary.approveSummary());

        return new MonthlySummaryDto(
                totalShippingQuantity,
                totalShippingQuantityMessage,
                totalExpenses,
                totalExpensesMessage,
                totalPartRequestCount,
                totalPartRequestCountMessage,
                totalOrderRequestCount,
                totalOrderApprovalCount,
                approvalRate,
                approvalRateMessage
        );
    }

    private MonthlyStatisticsDto getMonthlyStatistics(Report report) {
        return new MonthlyStatisticsDto(
                getShipmentQuantityListDto(report),
                getAutostockListDto(report),
                getExpenseListDto(report)
        );
    }

    private ShipmentQuantityListDto getShipmentQuantityListDto(Report report) {
        List<SupplyPartInfoDto> supplyPartInfoDtos = report.getSupplyQuantityGraph();

        List<StatisticsPartInfoDto> lastMonthList = new ArrayList<>();
        List<StatisticsPartInfoDto> currentMonthList = new ArrayList<>();
        List<StatisticsPartInfoDto> aiMonthList = new ArrayList<>();

        supplyPartInfoDtos.forEach(
                supplyPartInfoDto -> {
                    String partName = supplyPartInfoDto.partName();
                    lastMonthList.add(StatisticsPartInfoDto.of(partName, supplyPartInfoDto.lastMonthQuantity()));
                    currentMonthList.add(StatisticsPartInfoDto.of(partName, supplyPartInfoDto.currentMonthQuantity()));
                    aiMonthList.add(StatisticsPartInfoDto.of(partName, supplyPartInfoDto.predictQuantity()));
                }
        );

        return new ShipmentQuantityListDto(lastMonthList, currentMonthList, aiMonthList);
    }

    private AutoStockListDto getAutostockListDto(Report report) {
        List<PurchasePartAutoStockInfoDto> purchasePartAutoStockInfoDtos = report.getPurchaseAutoStockGraph();

        List<StatisticsPartInfoDto> lastMonthList = new ArrayList<>();
        List<StatisticsPartInfoDto> currentMonthList = new ArrayList<>();

        purchasePartAutoStockInfoDtos.forEach(
                purchasePartAutoStockInfoDto -> {
                    lastMonthList.add(StatisticsPartInfoDto.of((purchasePartAutoStockInfoDto.partName()), purchasePartAutoStockInfoDto.lastMonthQuantity()));
                    currentMonthList.add(StatisticsPartInfoDto.of((purchasePartAutoStockInfoDto.partName()), purchasePartAutoStockInfoDto.currentMonthQuantity()));
                }
        );

        return new AutoStockListDto(lastMonthList, currentMonthList);
    }

    private ExpenseListDto getExpenseListDto(Report report) {
        List<PurchasePartExpenseInfoDto> purchasePartExpenseInfoDtos = report.getPurchaseExpensesGraph();

        List<StatisticsPartInfoDto> lastMonthList = new ArrayList<>();
        List<StatisticsPartInfoDto> currentMonthList = new ArrayList<>();

        purchasePartExpenseInfoDtos.forEach(
                purchasePartExpenseInfoDto -> {
                    lastMonthList.add(StatisticsPartInfoDto.of((purchasePartExpenseInfoDto.partName()), purchasePartExpenseInfoDto.lastMonthTotalExpense()));
                    currentMonthList.add(StatisticsPartInfoDto.of((purchasePartExpenseInfoDto.partName()), purchasePartExpenseInfoDto.currentMonthTotalExpense()));
                }
        );

        return new ExpenseListDto(lastMonthList, currentMonthList);
    }


}
