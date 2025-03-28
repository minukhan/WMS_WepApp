package site.autoever.reportservice.report.application.port.out;

import site.autoever.reportservice.report.application.domain.Report;

import java.util.List;
import java.util.Optional;

public interface ReadReportPort {
    Optional<Report> getReport(int year, int month);

    Optional<Report> getReportById(String reportId);
    List<Report> getAllReports();
}
