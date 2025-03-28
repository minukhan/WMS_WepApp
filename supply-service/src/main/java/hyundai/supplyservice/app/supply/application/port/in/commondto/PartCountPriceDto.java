package hyundai.supplyservice.app.supply.application.port.in.commondto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PartCountPriceDto(
        @Schema(example = "GHP001")
        String partId,
        @Schema(example = "계기판")
        String partName,
        @Schema(description = "part quantity * 정해진 금액=price", example = "50000")
        Long price,
        @Schema(example = "5")
        int quantity
) {
}
