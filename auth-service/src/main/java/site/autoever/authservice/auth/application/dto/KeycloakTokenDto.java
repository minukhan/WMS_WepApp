package site.autoever.authservice.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KeycloakTokenDto(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken
) {
}
