package hyundai.supplyservice.app.supply.application.port.in.verify;

import io.swagger.v3.oas.annotations.media.Schema;

public record VerifyResultResponseDto (
        @Schema(example = "true")
        Boolean status,
        @Schema(example = "재고 충분")
        String message

){
}
