package hyundai.purchaseservice.purchase.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReportPurchasePartAutoStockInfoDto(
        @Schema(description = "품목 명", example = "엔진")
        String partName,

        @Schema(description = "이번 달 자동 발주 수량", example = "40")
        long currentMonthQuantity,

        @Schema(description = "지난 달 자동 발주 수량", example = "20")
        long lastMonthQuantity,

        @Schema(description = "변화량", example = "20")
        long changeQuantity,

        @Schema(description = "전월 대비 퍼센트", example = "100.0  또는 Double.MIN_VALUE")
        double changeRate
) {
    public static ReportPurchasePartAutoStockInfoDto of(String partName, long currentQuantity, long lastQuantity) {
        double changeRate = currentQuantity == 0 || lastQuantity == 0
                ? Double.MIN_VALUE
                : (double) (currentQuantity - lastQuantity) / lastQuantity * 100;

        return new ReportPurchasePartAutoStockInfoDto(
                partName,
                currentQuantity,
                lastQuantity,
                currentQuantity - lastQuantity,
                changeRate
        );
    }
}
