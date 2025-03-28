package hyundai.purchaseservice.purchase.adapter.in.dto;

import hyundai.purchaseservice.purchase.application.dto.MonthExpensesDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MonthExpensesResponse(
        @Schema(description = "월 리스트", example = "[2, 1, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3]")
        List<Integer> monthList,
        List<MonthExpensesDto> monthExpensesList
) {
}
