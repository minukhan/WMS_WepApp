package hyundai.safeservice.app.safe_Stock.adapter.dto;

public record AIModifyResponse(
        String message
) {
    public static AIModifyResponse from(String json) {

        return new AIModifyResponse(json);
    }

}
