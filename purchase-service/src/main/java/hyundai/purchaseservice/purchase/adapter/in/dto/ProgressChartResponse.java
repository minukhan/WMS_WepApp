package hyundai.purchaseservice.purchase.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ProgressChartResponse(
        @Schema(description = "현재 날짜", example = "2025-02-01")
        LocalDate date,
        @Schema(description = "입고 전체 개수", example = "80")
        Long requestedQuantity,
        @Schema(description = "입고 완료한 개수", example = "30")
        Long currentQuantity,
        @Schema(description = "진행 퍼센트", example = "37.50")
        Double progressRate
) {
    public static ProgressChartResponse of(Long totalAmount, Long processedAmount) {
        Double percent = totalAmount == 0L ? 0 : (double) processedAmount / totalAmount * 100;
        return new ProgressChartResponse(LocalDate.now(), totalAmount, processedAmount, percent);
    }
}
