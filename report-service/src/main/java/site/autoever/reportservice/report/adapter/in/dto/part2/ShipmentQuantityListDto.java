package site.autoever.reportservice.report.adapter.in.dto.part2;

import java.util.List;

public record ShipmentQuantityListDto(
        List<StatisticsPartInfoDto> lastMonthList,
        List<StatisticsPartInfoDto> currentMonthList,
        List<StatisticsPartInfoDto> aiList
) {
}
