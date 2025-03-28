package hyundai.supplyservice.app.supply.application.port.in.statistic;

import io.swagger.v3.oas.annotations.media.Schema;

public record MonthlyTotalCountResponseDto(
        @Schema(description = "수량 요약")
        CountSummary countSummary,
        @Schema(description = "승인 요약")
        ApproveSummary approveSummary
) {
    public record CountSummary(
            @Schema(example = "1200", description = "현재 월 총 부품 출고 박스 수")
            Long currentTotalPartBox,
            @Schema(example = "900", description = "전월 총 부품 출고 박스 수")
            Long lastMonthTotalPartBox,
            @Schema(example = "300", description = "변동 수량")
            Long changeQuantity,
            @Schema(example = "31.0", description = "변동률")
            double changeRate
    ) {}

    public record ApproveSummary(
            @Schema(example = "100", description = "현재 월 요청된 주문 수")
            int currentRequestedOrders,
            @Schema(example = "50", description = "현재 월 승인된 주문 수")
            int currentApprovedOrders,
            @Schema(example = "50", description = "현재 월 승인률")
            double currentApprovalRate,
            @Schema(example = "48", description = "전월 승인률")
            double lastMonthApprovalRate
    ) {}
}
