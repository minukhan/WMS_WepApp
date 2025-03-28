package site.autoever.authservice.auth.application.port.out;

import site.autoever.authservice.auth.application.dto.KeycloakTokenDto;

public interface KeycloakAuthPort {
    KeycloakTokenDto exchangeAdminCodeForToken(String authorizationCode);
    KeycloakTokenDto exchangeClientCodeForToken(String authorizationCode);
    KeycloakTokenDto exchangeWebViewCodeForToken(String authorizationCode);
}
