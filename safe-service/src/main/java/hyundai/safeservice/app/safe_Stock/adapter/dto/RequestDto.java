package hyundai.safeservice.app.safe_Stock.adapter.dto;


public record RequestDto(
        int safeQuantity,
        String reason
) {
}
