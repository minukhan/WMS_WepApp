package hyundai.safeservice.app.propriety_Stock.adapter.dto;


public record RequestProprietyDto(
        String partId,
        int safeQuantity,
        String reason
) {
}
