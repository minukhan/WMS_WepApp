package site.autoever.reportservice.report.application.port.in;

public interface GenerateMonthlyReportUseCase {
    void generateMonthlyReport();

    void collectPurchaseData();

    void collectSupplyData();

    void processData();

    void collectSafeData();

    void sendAlarm();
}
