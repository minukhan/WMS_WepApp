package hyundai.purchaseservice.purchase.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddProcessedQuantityRequest (
        @Schema(description = "품목 코드", example = "BHP100")
        String partId,
        @Schema(description = "입고 구역", example = "A구역 1열 2층")
        String sectionName
){
}
