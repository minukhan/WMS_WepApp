package site.autoever.reportservice.report.application.port.in;

import site.autoever.reportservice.report.adapter.in.dto.ReadReportResponse;

public interface UpdateReportUseCase {

    ReadReportResponse updateReportStatus(String reportId);
}
