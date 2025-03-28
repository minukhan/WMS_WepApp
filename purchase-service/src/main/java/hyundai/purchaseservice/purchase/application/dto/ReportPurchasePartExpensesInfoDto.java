package hyundai.purchaseservice.purchase.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReportPurchasePartExpensesInfoDto(
        @Schema(description = "품목 명", example = "엔진")
        String partName,

        @Schema(description = "이번 달 지출비", example = "12000000")
        long currentMonthTotalExpense,

        @Schema(description = "지난 달 지출비", example = "9000000")
        long lastMonthTotalExpense,

        @Schema(description = "변화량", example = "3000000")
        long changeExpense,

        @Schema(description = "전월 대비 퍼센트", example = "33.333333  또는 Double.MIN_VALUE")
        double changeRate
) {
    public static ReportPurchasePartExpensesInfoDto of(String partName, long currentExpense, long lastMonthExpense){
        double changeRate = currentExpense == 0 || lastMonthExpense == 0
                ? Double.MIN_VALUE
                : (double) (currentExpense - lastMonthExpense) / lastMonthExpense * 100;


        return new ReportPurchasePartExpensesInfoDto(
                partName,
                currentExpense,
                lastMonthExpense,
                currentExpense - lastMonthExpense,
                changeRate
        );
    }
}
