package hyundai.purchaseservice.purchase.adapter.out.dto;

public record GetMonthRequestDto(
        Integer year,
        Integer month,
        Long requestCount
) {
}
