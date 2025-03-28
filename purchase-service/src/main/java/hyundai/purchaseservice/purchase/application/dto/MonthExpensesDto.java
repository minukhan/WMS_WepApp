package hyundai.purchaseservice.purchase.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MonthExpensesDto(
        @Schema(description = "월", example = "2")
        Integer month,
        @Schema(description = "지출비", example = "13654000")
        Long monthExpenses
) {
}
