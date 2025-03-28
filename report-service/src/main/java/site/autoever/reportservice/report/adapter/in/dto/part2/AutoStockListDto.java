package site.autoever.reportservice.report.adapter.in.dto.part2;

import java.util.List;

public record AutoStockListDto(
        List<StatisticsPartInfoDto> lastMonthList,
        List<StatisticsPartInfoDto> currentMonthList
) {
}
