package hyundai.purchaseservice.purchase.application.dto;

import hyundai.purchaseservice.purchase.adapter.out.dto.GetProgressPercentResponse;
import io.swagger.v3.oas.annotations.media.Schema;

public record ExpectedQuantityTomorrowDto(
        @Schema(description = "품목 코드", example = "BHP166")
        String partId,
        @Schema(description = "입고 수량", example = "22")
        Integer quantity,
        @Schema(description = "구역 id", example = "1")
        Long sectionId
) {
    public static ExpectedQuantityTomorrowDto of(GetProgressPercentResponse response) {
        return new ExpectedQuantityTomorrowDto(
                response.partId(),
                response.requestedQuantity(),
                response.sectionId()
        );
    }
}
