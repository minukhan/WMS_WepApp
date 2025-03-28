package site.autoever.verifyservice.verify.application.port.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PurchaseResponseDto(
        long total,
        @JsonProperty("parts")
        List<PurchaseInfoDto> purchaseInfos
) {
}
