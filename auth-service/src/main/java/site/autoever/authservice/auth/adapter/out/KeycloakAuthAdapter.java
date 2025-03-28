package site.autoever.authservice.auth.adapter.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import site.autoever.authservice.auth.application.dto.KeycloakTokenDto;
import site.autoever.authservice.auth.application.port.out.KeycloakAuthPort;

@Component
@Slf4j
public class KeycloakAuthAdapter implements KeycloakAuthPort {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String tokenUri;
    private final RestTemplate restTemplate;
    private final String redirectAdminUri;
    private final String redirectClientUri;
    private final String redirectWebViewUri;

    public KeycloakAuthAdapter(
            RestTemplate restTemplate,
            @Value("${keycloak.client-id}") String clientId,
            @Value("${keycloak.client-secret}") String clientSecret,
            @Value("${keycloak.redirect-uri}") String redirectUri,
            @Value("${keycloak.token-uri}") String tokenUri,
            @Value("${keycloak.redirect-detail.redirect-client}") String redirectClientUri,
            @Value("${keycloak.redirect-detail.redirect-admin}") String redirectAdminUri,
            @Value("${keycloak.redirect-detail.redirect-webview}") String redirectWebViewUri
    ) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
        this.redirectAdminUri = redirectAdminUri;
        this.redirectClientUri = redirectClientUri;
        this.redirectWebViewUri = redirectWebViewUri;
    }


    @Override
    public KeycloakTokenDto exchangeAdminCodeForToken(String authorizationCode) {
        return exchangeCodeForToken(authorizationCode, redirectAdminUri);
    }

    @Override
    public KeycloakTokenDto exchangeClientCodeForToken(String authorizationCode) {
        return exchangeCodeForToken(authorizationCode, redirectClientUri);
    }

    @Override
    public KeycloakTokenDto exchangeWebViewCodeForToken(String authorizationCode) {
        return exchangeCodeForToken(authorizationCode, redirectWebViewUri);
    }

    /**
     * Keycloak에서 Authorization Code를 Access Token으로 교환하는 공통 메서드
     */
    private KeycloakTokenDto exchangeCodeForToken(String authorizationCode, String redirectPath) {
        log.info("전달된 인증 코드 : {}", authorizationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String redirect = redirectUri + redirectPath;
        log.info("redirect url : {}", redirect);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", authorizationCode);
        body.add("redirect_uri", redirect);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<KeycloakTokenDto> response = restTemplate.exchange(
                tokenUri,
                HttpMethod.POST,
                requestEntity,
                KeycloakTokenDto.class
        );

        log.info("키클락 서버로부터 OAuth2 토큰 받기 성공");
        return response.getBody();
    }

}
