package hyundai.purchaseservice.purchase.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ExpensesDto(
        @Schema(description = "카테고리 또는 부품 이름. x축에 해당한다.", example = "카테고리 또는 부품 이름")
        String expensesItemName,
        @Schema(description = "지출비. y축에 해당한다.", example = "2141000")
        Long totalPrice
) {
}
