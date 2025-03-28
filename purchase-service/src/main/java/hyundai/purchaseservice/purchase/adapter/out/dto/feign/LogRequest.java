package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

public record LogRequest(
        Long userId,
        String message
) {
}
