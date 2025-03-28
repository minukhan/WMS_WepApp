package site.autoever.reportservice.report.adapter.in.dto.part2;

import java.util.List;

public record ExpenseListDto(
        List<StatisticsPartInfoDto> lastMonthList,
        List<StatisticsPartInfoDto> currentMonthList
) {
}
