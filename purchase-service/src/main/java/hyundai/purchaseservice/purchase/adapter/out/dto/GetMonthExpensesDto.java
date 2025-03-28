package hyundai.purchaseservice.purchase.adapter.out.dto;

public record GetMonthExpensesDto(
        Integer year,
        Integer month,
        Long expenses
) {
}
