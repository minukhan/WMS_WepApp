package site.autoever.reportservice.report.application.port.out;

import site.autoever.reportservice.report.application.domain.Report;

public interface CreateReportPort {
    void save(Report report);
}
