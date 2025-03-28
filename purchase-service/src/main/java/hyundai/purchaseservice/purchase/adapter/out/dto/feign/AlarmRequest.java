package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

public record AlarmRequest(
        String role,
        String message,
        String type
) {
}
