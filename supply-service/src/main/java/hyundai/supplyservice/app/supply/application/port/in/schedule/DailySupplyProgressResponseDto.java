package hyundai.supplyservice.app.supply.application.port.in.schedule;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record DailySupplyProgressResponseDto(
        @Schema(example = "2025-03-20")
        LocalDate date,
        @Schema(example = "400")
        int requestedQuantity,
        @Schema(example = "100")
        int currentQuantity,
        @Schema(example = "25.5")
        double progressRate  // 진행률을 %로 표시
) {
}
