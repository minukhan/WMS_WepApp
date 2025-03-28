package hyundai.purchaseservice.purchase.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReportPurchaseRequestDto(
        @Schema(description = "이번 달 요청 수", example = "100")
        long currentRequestOrders,

        @Schema(description = "지난 달 요청 수", example = "50")
        long lastMonthRequestOrders,

        @Schema(description = "변화량", example = "50")
        long changeQuantity,

        @Schema(description = "전월 대비 퍼센트", example = "100.0  또는 Double.MIN_VALUE")
        double changeRate
) {
    public static ReportPurchaseRequestDto to(long currentRequest, long lastMonthRequest){
        double changeRate = currentRequest == 0 || lastMonthRequest == 0
                ? Double.MIN_VALUE
                : (double) (currentRequest - lastMonthRequest) / lastMonthRequest * 100;

        return new ReportPurchaseRequestDto(
                currentRequest,
                lastMonthRequest,
                currentRequest - lastMonthRequest,
                changeRate
        );
    }
}

