package site.autoever.reportservice.report.application.dto.parts;

import java.util.List;

public record PartSummaryDto(
        List<PartInfoWithAIDto> parts
) {
}
