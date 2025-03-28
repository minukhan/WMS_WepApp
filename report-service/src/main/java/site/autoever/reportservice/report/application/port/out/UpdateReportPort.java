package site.autoever.reportservice.report.application.port.out;

import site.autoever.reportservice.report.application.domain.Report;

public interface UpdateReportPort {

    Report updateReport(String reportId);

}
