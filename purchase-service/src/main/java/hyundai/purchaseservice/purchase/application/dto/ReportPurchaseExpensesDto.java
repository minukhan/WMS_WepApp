package hyundai.purchaseservice.purchase.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReportPurchaseExpensesDto(
        @Schema(description = "이번 달 지출비", example = "12000000")
        long currentTotalExpense,

        @Schema(description = "지난 달 지출비", example = "9000000")
        long lastMonthTotalExpense,

        @Schema(description = "변화량", example = "3000000")
        long changeQuantity,

        @Schema(description = "전월 대비 퍼센트", example = "33.333333  또는 Double.MIN_VALUE")
        double changeRate
) {
    public static ReportPurchaseExpensesDto to(long currentExpense, long lastMonthExpense){
        double changeRate = currentExpense == 0 || lastMonthExpense == 0
                ? Double.MIN_VALUE
                : (double) (currentExpense - lastMonthExpense) / lastMonthExpense * 100;

        return new ReportPurchaseExpensesDto(
                currentExpense,
                lastMonthExpense,
                currentExpense - lastMonthExpense,
                changeRate
        );
    }
}

