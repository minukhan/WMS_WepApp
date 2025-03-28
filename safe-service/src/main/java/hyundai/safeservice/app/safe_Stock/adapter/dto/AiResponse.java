package hyundai.safeservice.app.safe_Stock.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AiResponse(

        @JsonProperty("part_id")
        String partId,

        @JsonProperty("current_month_sales")
        int currentMonthSales,

        @JsonProperty("predicted_next_month_sales")
        int predictedNextMonthSales
) {
}
