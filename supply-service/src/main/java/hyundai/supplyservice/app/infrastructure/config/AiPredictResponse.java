package hyundai.supplyservice.app.infrastructure.config;

public record AiPredictResponse(
        String part_id,
        int current_month_sales,
        int predicted_next_month_sales

) {
}
