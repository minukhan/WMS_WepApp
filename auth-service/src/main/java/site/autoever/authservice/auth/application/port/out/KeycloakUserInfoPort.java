package site.autoever.authservice.auth.application.port.out;

import site.autoever.authservice.auth.application.dto.KeycloakUserInfoDto;

public interface KeycloakUserInfoPort {
    KeycloakUserInfoDto fetchUserInfo(String accessToken);
}
