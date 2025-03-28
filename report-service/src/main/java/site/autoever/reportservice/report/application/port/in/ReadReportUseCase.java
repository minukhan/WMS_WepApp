package site.autoever.reportservice.report.application.port.in;


import site.autoever.reportservice.report.adapter.in.dto.ReadReportResponse;

public interface ReadReportUseCase {
    ReadReportResponse readReport(int year, int month);

    ReadReportResponse readReportById(String reportId);
}
