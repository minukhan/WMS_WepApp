package site.autoever.reportservice.report.application.dto.supply.part2;

import site.autoever.reportservice.report.application.dto.supply.SupplyPartInfoDto;

import java.util.List;

public record SupplyQuantitySummaryDto(
        List<SupplyPartInfoDto> parts
) {
}
