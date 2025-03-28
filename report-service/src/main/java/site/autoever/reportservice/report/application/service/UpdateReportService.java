package site.autoever.reportservice.report.application.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.autoever.reportservice.infrastructure.util.MessageGenerator;
import site.autoever.reportservice.infrastructure.util.UserIdResolver;
import site.autoever.reportservice.report.adapter.in.dto.ReadReportResponse;
import site.autoever.reportservice.report.adapter.in.dto.part1.MonthlySummaryDto;
import site.autoever.reportservice.report.adapter.in.dto.part2.AutoStockListDto;
import site.autoever.reportservice.report.adapter.in.dto.part2.ExpenseListDto;
import site.autoever.reportservice.report.adapter.in.dto.part2.MonthlyStatisticsDto;
import site.autoever.reportservice.report.adapter.in.dto.part2.ShipmentQuantityListDto;
import site.autoever.reportservice.report.adapter.in.dto.part2.StatisticsPartInfoDto;
import site.autoever.reportservice.report.application.domain.Report;
import site.autoever.reportservice.report.application.dto.alarm.AlarmCreateRequest;
import site.autoever.reportservice.report.application.dto.log.LogCreateRequest;
import site.autoever.reportservice.report.application.dto.parts.PartInfoWithAIDtoList;
import site.autoever.reportservice.report.application.dto.purchase.PurchasePartAutoStockInfoDto;
import site.autoever.reportservice.report.application.dto.purchase.PurchasePartExpenseInfoDto;
import site.autoever.reportservice.report.application.dto.purchase.part1.PurchaseSummaryDto;
import site.autoever.reportservice.report.application.dto.supply.SupplyPartInfoDto;
import site.autoever.reportservice.report.application.dto.supply.part1.SupplySummaryDto;
import site.autoever.reportservice.report.application.port.in.UpdateReportUseCase;
import site.autoever.reportservice.report.application.port.out.CreateAlarmPort;
import site.autoever.reportservice.report.application.port.out.CreateLogPort;
import site.autoever.reportservice.report.application.port.out.UpdateReportPort;

@Service
@RequiredArgsConstructor
public class UpdateReportService implements UpdateReportUseCase {

    private final UpdateReportPort updateReportPort;
    private final MessageGenerator messageGenerator;

    private final CreateAlarmPort createAlarmPort;
    private final CreateLogPort createLogPort;

    private final UserIdResolver userIdResolver;

    @Override
    public ReadReportResponse updateReportStatus(String reportId) {
        Report updatedReport = updateReportPort.updateReport(reportId);

        MonthlySummaryDto monthlySummaryDto = getMonthlySummary(updatedReport);

        MonthlyStatisticsDto monthlyStatisticsDto = getMonthlyStatistics(updatedReport);

        AlarmCreateRequest alarmCreateRequest = new AlarmCreateRequest(
                "ROLE_INFRA_MANAGER",
                String.format("%d년 %d월 월간 보고서의 AI 자동 발주 되었습니다.", updatedReport.getYear(),
                        updatedReport.getMonth()),
                "REPORT"
        );

        createAlarmPort.createAlarm(alarmCreateRequest);

        LogCreateRequest logCreateRequest = new LogCreateRequest(
                userIdResolver.getCurrentUserId(),
                String.format(("사용자가 %d년 %d월 보고서의 전체 부품 AI 발주를 신청하였습니다."), updatedReport.getYear(),
                        updatedReport.getMonth()));

        createLogPort.saveLog(logCreateRequest);

        return new ReadReportResponse(updatedReport.getId(), updatedReport.isModified(),
                updatedReport.getYear(), updatedReport.getMonth(), monthlySummaryDto,
                monthlyStatisticsDto,
                new PartInfoWithAIDtoList(updatedReport.getPartWithAiGraph()));
    }

    private MonthlySummaryDto getMonthlySummary(Report report) {
        SupplySummaryDto supplySummary = report.getSupplySummary();
        PurchaseSummaryDto purchaseSummary = report.getPurchaseSummary();

        long totalShippingQuantity = supplySummary.countSummary().currentTotalPartBox();
        String totalShippingQuantityMessage = messageGenerator.getTotalShippingQuantityMessage(
                supplySummary.countSummary());

        long totalExpenses = purchaseSummary.expenseSummary().currentTotalExpense();
        String totalExpensesMessage = messageGenerator.getTotalExpensesMessage(
                purchaseSummary.expenseSummary());

        long totalPartRequestCount = purchaseSummary.requestSummary().currentRequestOrders();
        String totalPartRequestCountMessage = messageGenerator.getTotalPartRequestCountMessage(
                purchaseSummary.requestSummary());

        long totalOrderRequestCount = supplySummary.approveSummary().currentRequestedOrders();
        long totalOrderApprovalCount = supplySummary.approveSummary().currentApprovedOrders();
        double approvalRate = supplySummary.approveSummary().currentApprovalRate();
        String approvalRateMessage = messageGenerator.getApprovalRateMessage(
                supplySummary.approveSummary());

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
                    lastMonthList.add(StatisticsPartInfoDto.of(partName,
                            supplyPartInfoDto.lastMonthQuantity()));
                    currentMonthList.add(StatisticsPartInfoDto.of(partName,
                            supplyPartInfoDto.currentMonthQuantity()));
                    aiMonthList.add(StatisticsPartInfoDto.of(partName,
                            supplyPartInfoDto.predictQuantity()));
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
                    lastMonthList.add(
                            StatisticsPartInfoDto.of((purchasePartAutoStockInfoDto.partName()),
                                    purchasePartAutoStockInfoDto.lastMonthQuantity()));
                    currentMonthList.add(
                            StatisticsPartInfoDto.of((purchasePartAutoStockInfoDto.partName()),
                                    purchasePartAutoStockInfoDto.currentMonthQuantity()));
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
                    lastMonthList.add(
                            StatisticsPartInfoDto.of((purchasePartExpenseInfoDto.partName()),
                                    purchasePartExpenseInfoDto.lastMonthTotalExpense()));
                    currentMonthList.add(
                            StatisticsPartInfoDto.of((purchasePartExpenseInfoDto.partName()),
                                    purchasePartExpenseInfoDto.currentMonthTotalExpense()));
                }
        );

        return new ExpenseListDto(lastMonthList, currentMonthList);
    }

}
