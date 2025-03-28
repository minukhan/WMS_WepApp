package hyundai.supplyservice.app.supply.application.port.in.commondto;

import io.swagger.v3.oas.annotations.media.Schema;

public record PartInfoDto(
        @Schema(example = "GHP001")
        String id,
        @Schema(example = "계기판")
        String partName
) {
    }
