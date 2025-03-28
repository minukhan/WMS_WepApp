package site.autoever.authservice.auth.adapter.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import site.autoever.authservice.auth.application.dto.KeycloakUserInfoDto;
import site.autoever.authservice.auth.application.port.out.KeycloakUserInfoPort;
@Slf4j
@Component
public class KeycloakUserInfoAdapter implements KeycloakUserInfoPort {
    private final String userInfoUri;
    private final RestTemplate restTemplate;

    public KeycloakUserInfoAdapter(
            RestTemplate restTemplate,
            @Value("${keycloak.user-info-uri}") String userInfoUri
    ) {
        this.restTemplate = restTemplate;
        this.userInfoUri = userInfoUri;
    }

    @Override
    public KeycloakUserInfoDto fetchUserInfo(String accessToken) {
        log.info("전달된 access Token : {}", accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<KeycloakUserInfoDto> response = restTemplate.exchange(
                userInfoUri,
                HttpMethod.GET,
                requestEntity,
                KeycloakUserInfoDto.class
        );

        log.info("키클락 유저 정보 조회 성공");
        return response.getBody();
    }
}
