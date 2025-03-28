package hyundai.supplyservice.app.supply.application.port.in.schedule;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record DailyPartQuantity(
        @Schema(example = "계기판")
        String partName,
        @Schema( description = "출고해야 하는 개수", example = "500")
        int requestedQuantity,
        @Schema(example = "100")
        int currentQuantity

) {
}
