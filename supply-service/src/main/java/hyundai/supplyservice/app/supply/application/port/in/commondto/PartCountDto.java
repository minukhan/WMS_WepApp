package hyundai.supplyservice.app.supply.application.port.in.commondto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PartCountDto(
        @Schema(example = "GHP001")
        String partId,
        @Schema(example="100")
        int quantity
) {
}
