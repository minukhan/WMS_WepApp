package hyundai.supplyservice.app.supply.application.port.in.verify;

import hyundai.supplyservice.app.supply.application.port.in.commondto.PartCountDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PartCountResponseDto(
        @Schema(example = "500")
        Long total,
        List<PartCountDto> parts
) {
}
