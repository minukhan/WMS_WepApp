package site.autoever.reportservice.report.adapter.in.dto.part3;

import site.autoever.reportservice.report.application.dto.parts.PartInfoWithAIDto;

import java.util.List;

public record MonthlyAIRecommendDto(
        List<PartInfoWithAIDto> recommendedPartsList
) {
}
