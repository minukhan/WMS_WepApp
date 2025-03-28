package site.autoever.reportservice.report.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.autoever.reportservice.report.application.dto.alarm.AlarmCreateRequest;
import site.autoever.reportservice.report.application.dto.parts.PartInfoWithAIDtoList;
import site.autoever.reportservice.report.application.dto.purchase.part1.PurchaseSummaryDto;
import site.autoever.reportservice.report.application.dto.purchase.part2.PurchaseAutoStockSummaryDto;
import site.autoever.reportservice.report.application.dto.purchase.part2.PurchaseExpensesSummaryDto;
import site.autoever.reportservice.report.application.dto.supply.SupplyPartInfoDto;
import site.autoever.reportservice.report.application.dto.supply.part1.SupplySummaryDto;
import site.autoever.reportservice.report.application.dto.supply.part2.SupplyQuantitySummaryDto;
import site.autoever.reportservice.report.application.domain.Report;
import site.autoever.reportservice.report.application.port.in.GenerateMonthlyReportUseCase;
import site.autoever.reportservice.report.application.port.out.CreateAlarmPort;
import site.autoever.reportservice.report.application.port.out.CreateReportPort;
import site.autoever.reportservice.report.application.port.out.PurchaseReportPort;
import site.autoever.reportservice.report.application.port.out.SafeReportPort;
import site.autoever.reportservice.report.application.port.out.SupplyReportPort;

import java.time.Instant;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenerateMonthlyReportService implements GenerateMonthlyReportUseCase {

    private final SupplyReportPort supplyReportPort;
    private final PurchaseReportPort purchaseReportPort;
    private final SafeReportPort safeReportPort;
    private final CreateReportPort createReportPort;
    private final CreateAlarmPort createAlarmPort;

    private PurchaseSummaryDto purchaseSummary;
    private PurchaseAutoStockSummaryDto autoStockSummary;
    private PurchaseExpensesSummaryDto expensesSummary;
    private SupplySummaryDto supplySummary;
    private SupplyQuantitySummaryDto quantitySummary;
    private PartInfoWithAIDtoList partInfoWithAIDtoList;

    // Step 1-1: Purchase 데이터 수집
    @Override
    public void collectPurchaseData() {
        YearMonth previousYearMonth = getPreviousYearMonth();
        int year = previousYearMonth.getYear();
        int month = previousYearMonth.getMonthValue();

        log.info("📅 [{}-{}] Purchase 데이터 수집을 시작합니다.", year, month);

        try {
            purchaseSummary = purchaseReportPort.getPurchaseSummary(year, month);
            autoStockSummary = purchaseReportPort.getPurchaseAutoStockSummary(year, month);
            expensesSummary = purchaseReportPort.getPurchaseExpensesSummary(year, month);

            if (purchaseSummary == null || autoStockSummary == null || expensesSummary == null) {
                throw new RuntimeException("구매 데이터 수집 실패");
            }

            log.info("✅ Purchase 데이터 수집 완료.");
        } catch (Exception e) {
            log.error("❌ Purchase 데이터 수집 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("Purchase 데이터 수집 실패: " + e.getMessage(), e);
        }
    }

    // Step 1-2: Supply 데이터 수집
    @Override
    public void collectSupplyData() {
        YearMonth previousYearMonth = getPreviousYearMonth();
        int year = previousYearMonth.getYear();
        int month = previousYearMonth.getMonthValue();

        log.info("📅 [{}-{}] Supply 데이터 수집을 시작합니다.", year, month);

        try {
            supplySummary = supplyReportPort.getSupplySummary(year, month);
            quantitySummary = supplyReportPort.getSupplyQuantitySummary(year, month);

            if (supplySummary == null || quantitySummary == null) {
                throw new RuntimeException("Supply 데이터 수집 실패");
            }

            log.info("✅ Supply 데이터 수집 완료.");
        } catch (Exception e) {
            log.error("❌ Supply 데이터 수집 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("Supply 데이터 수집 실패: " + e.getMessage(), e);
        }
    }

    // Step 1-3: Safe 데이터 수집
    @Override
    public void collectSafeData() {
        YearMonth previousYearMonth = getPreviousYearMonth();
        int year = previousYearMonth.getYear();
        int month = previousYearMonth.getMonthValue();

        log.info("📅 [{}-{}] Safe 데이터 수집을 시작합니다.", year, month);

        try {
            partInfoWithAIDtoList = safeReportPort.getSafetyInfo(year, month);

            if (partInfoWithAIDtoList == null || partInfoWithAIDtoList.partInfoWithAIDtoList().isEmpty()) {
                throw new RuntimeException("Safe 데이터 수집 실패: 데이터가 없습니다.");
            }

            log.info("✅ Safe 데이터 수집 완료.");
        } catch (Exception e) {
            log.error("❌ Safe 데이터 수집 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("Safe 데이터 수집 실패: " + e.getMessage(), e);
        }
    }

    // Step 2: 데이터 처리
    @Override
    public void processData() {
        log.info("⚙️ 데이터 처리를 시작합니다.");

        if (purchaseSummary == null || supplySummary == null || partInfoWithAIDtoList == null) {
            log.error("⚠️ 데이터 처리 실패: 수집된 데이터가 없습니다.");
            throw new IllegalStateException("데이터 처리 실패: 수집된 데이터가 없습니다.");
        }

        log.info("✅ 데이터 처리 완료.");
    }

    // Step 3: 보고서 생성 및 저장
    @Override
    public void generateMonthlyReport() {
        YearMonth previousYearMonth = getPreviousYearMonth();
        int year = previousYearMonth.getYear();
        int month = previousYearMonth.getMonthValue();

        log.info("📊 [{}-{}] 보고서 생성을 시작합니다.", year, month);

        try {
            Report report = buildReport(year, month);
            createReportPort.save(report);
            log.info("✅ 보고서 생성 및 저장 완료.");
        } catch (Exception e) {
            log.error("❌ 보고서 생성 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("보고서 생성 실패: " + e.getMessage(), e);
        }
    }

    // Step 4: 알람 발송
    @Override
    public void sendAlarm() {
        YearMonth previousYearMonth = getPreviousYearMonth();
        int year = previousYearMonth.getYear();
        int month = previousYearMonth.getMonthValue();

        log.info("📣 알람 발송을 시작합니다.");

        try {
            AlarmCreateRequest alarmRequest = createAlarm(year, month);
            createAlarmPort.createAlarm(alarmRequest);
            log.info("✅ 알람 발송 완료.");
        } catch (Exception e) {
            log.error("❌ 알람 발송 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("알람 발송 실패: " + e.getMessage(), e);
        }
    }

    private Report buildReport(int year, int month) {
        log.info("📝 Report 객체 생성 중...");

        return Report.builder()
                .year(year)
                .month(month)
                .purchaseSummary(purchaseSummary)
                .supplySummary(supplySummary)
                .purchaseAutoStockGraph(autoStockSummary.parts())
                .purchaseExpensesGraph(expensesSummary.parts())
                .supplyQuantityGraph(quantitySummary.parts())
                .partWithAiGraph(partInfoWithAIDtoList.partInfoWithAIDtoList())
                .isModified(false)
                .createdAt(Instant.now())
                .build();
    }

    private YearMonth getPreviousYearMonth() {
        return YearMonth.now();
    }

    private AlarmCreateRequest createAlarm(int year, int month) {
        return new AlarmCreateRequest(
                "ROLE_INFRA_MANAGER",
                String.format("%d년 %d월 월간 보고서가 발행되었습니다.", year, month),
                "REPORT"
        );
    }
}
