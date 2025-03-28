package site.autoever.reportservice.report.adapter.in.dto;

import site.autoever.reportservice.report.adapter.in.dto.part1.MonthlySummaryDto;
import site.autoever.reportservice.report.adapter.in.dto.part2.MonthlyStatisticsDto;
import site.autoever.reportservice.report.application.dto.parts.PartInfoWithAIDtoList;

public record ReadReportResponse(
        String id,
        boolean isModified,
        long year,
        long month,
        MonthlySummaryDto monthlySummary,
        MonthlyStatisticsDto monthlyStatistics,
        PartInfoWithAIDtoList partWithAiRecommends
) {
}
