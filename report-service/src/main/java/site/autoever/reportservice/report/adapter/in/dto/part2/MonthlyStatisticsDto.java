package site.autoever.reportservice.report.adapter.in.dto.part2;

public record MonthlyStatisticsDto(
        ShipmentQuantityListDto shipmentQuantityInfoList,
        AutoStockListDto autoStockInfoList,
        ExpenseListDto expenseInfoList
) {
}
