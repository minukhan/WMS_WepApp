package hyundai.purchaseservice.purchase.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PartQuantityInfoDto (
        @Schema(description = "품목 코드")
        String partId,
        @Schema(description = "입고 예정 수량")
        Long currentStock
){
}
