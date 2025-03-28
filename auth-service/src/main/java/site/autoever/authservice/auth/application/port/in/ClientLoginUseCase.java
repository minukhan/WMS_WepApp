package site.autoever.authservice.auth.application.port.in;

import site.autoever.authservice.auth.adapter.in.dto.Oauth2UserTokenResponse;

public interface ClientLoginUseCase {
    Oauth2UserTokenResponse getToken(String authorizationCode);
}
