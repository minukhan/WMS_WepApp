package site.autoever.reportservice.report.adapter.in.dto;

import java.util.List;

public record ShowAllReportResponse(
        List<ReportSummaryDto> reports
) {
}
