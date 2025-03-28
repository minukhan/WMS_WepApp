package site.autoever.verifyservice.verify.application.port.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PartInfoDto(
        String partId,
        @JsonProperty("currentStock")
        long quantity,            // JSON 필드명: currentStock → 필드: quantity
        @JsonProperty("estimatedDeliveryDays")
        long deliveryDuration // JSON 필드명: estimatedDeliveryDays → 필드: deliveryDuration
) {
}
