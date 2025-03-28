package hyundai.storageservice.app.storage_schedule.adapter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ProgressChartResponse(
        @Schema(description = "현재 날짜", example = "2025-02-01")
        LocalDate date,
        @Schema(description = "입고 전체 개수", example = "80")
        int requestedQuantity,
        @Schema(description = "입고 완료한 개수", example = "30")
        int currentQuantity,
        @Schema(description = "진행 퍼센트", example = "37.50")
        Double progressRate
) {
    public static ProgressChartResponse of(int totalAmount, int processedAmount) {
        Double percent = totalAmount == 0L ? 0 : (double) processedAmount / totalAmount * 100;
        return new ProgressChartResponse(LocalDate.now(), totalAmount, processedAmount, percent);
    }
}
