package site.autoever.reportservice.report.adapter.in.dto;

public record ReportSummaryDto(
        String reportId,
        int year,
        int month,
        boolean isModified
) {
}
