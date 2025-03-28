package site.autoever.reportservice.report.adapter.in.dto.part2;

public record StatisticsPartInfoDto(
        String partName,
        long quantity
) {
    public static StatisticsPartInfoDto of(String partName, long quantity) {
        return new StatisticsPartInfoDto(partName, quantity);
    }
}
