package hyundai.supplyservice.app.supply.application.port.in.schedule;

import io.swagger.v3.oas.annotations.media.Schema;

public record PartSectionQuantityDto(
        @Schema(example = "계기판")
        String partName,
        @Schema(example = "100")
        int requestedQuantity,
        @Schema(example = "0")
        int currentQuantity,
        @Schema(example = "A구역")
        String sectionName,
        @Schema(example = "2열 1층")
        String sectionFloor
){

  }
