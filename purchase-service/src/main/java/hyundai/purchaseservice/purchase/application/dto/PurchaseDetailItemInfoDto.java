package hyundai.purchaseservice.purchase.application.dto;

import hyundai.purchaseservice.purchase.adapter.out.dto.SearchResponse;
import io.swagger.v3.oas.annotations.media.Schema;

public record PurchaseDetailItemInfoDto(
        @Schema(description = "품목 명", example = "엔진 오일")
        String partName,
        @Schema(description = "품목 코드", example = "PH-100")
        String partId,
        @Schema(description = "수량", example = "30")
        Integer quantity
) {
        public static PurchaseDetailItemInfoDto of(SearchResponse searchResponse, String name) {
                return new PurchaseDetailItemInfoDto(
                        name,
                        searchResponse.partId(),
                        searchResponse.quantity()
                );
        }
}