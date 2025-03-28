package hyundai.safeservice.app.safe_Stock.adapter.dto;

public record SafeRequest(
        String partId,
        int modifySafeQuantity,
        int modifyProprietyQuantity
) {
    public static SafeRequest of(String partId, int modifySafeQuantity, int modifyProprietyQuantity) {
        return new SafeRequest(partId, modifySafeQuantity, modifyProprietyQuantity);
    }
}
