package hyundai.supplyservice.app.supply.application.port.in.statistic;

import io.swagger.v3.oas.annotations.media.Schema;

public record TopPartDetail(
        @Schema(example = "GHP001")
        String partId,
        @Schema(example = "계기판")
        String partName,
        @Schema(example = "기타")
        String category,
        @Schema(description = "특정 부품의 한달 출고량", example = "500")
        int totalQuantity,
        @Schema(description = "한달 전체 출고량 대비 해당 부품의 출고량 비율, %", example = "50.5")
        double percentageOfTotal
) {
}
