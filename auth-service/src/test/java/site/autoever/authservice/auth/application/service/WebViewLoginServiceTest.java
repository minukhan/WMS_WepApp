package site.autoever.authservice.auth.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.autoever.authservice.auth.adapter.in.dto.Oauth2UserTokenResponse;
import site.autoever.authservice.auth.application.dto.KeycloakTokenDto;
import site.autoever.authservice.auth.application.dto.KeycloakUserInfoDto;
import site.autoever.authservice.auth.application.dto.UserInfoDto;
import site.autoever.authservice.auth.application.dto.UserSummaryDto;
import site.autoever.authservice.auth.application.port.out.KeycloakAuthPort;
import site.autoever.authservice.auth.application.port.out.KeycloakUserInfoPort;
import site.autoever.authservice.auth.application.port.out.RegisterUserPort;
import site.autoever.authservice.infrastructure.security.jwt.JwtTokenProvider;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("🔑 Webview 로그인 서비스 테스트")
class WebViewLoginServiceTest {

    @InjectMocks
    private WebViewLoginService webViewLoginService;

    @Mock
    private KeycloakAuthPort keycloakAuthPort;

    @Mock
    private KeycloakUserInfoPort keycloakUserInfoPort;

    @Mock
    private RegisterUserPort registerUserPort;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("✅ 인증 코드를 사용하여 OAuth2 토큰을 성공적으로 발급한다.")
    @Test
    void getToken_ShouldReturnOauth2UserTokenResponse() {
        // Given
        String authorizationCode = "test-auth-code";
        KeycloakTokenDto keycloakTokenDto = new KeycloakTokenDto("access-token", "refresh-token");

        KeycloakUserInfoDto keycloakUserInfoDto = new KeycloakUserInfoDto(
                "user-id", "홍길동", true, Set.of("ROLE_USER"), "010-1234-5678",
                "홍길동", "hong@example.com", "Seoul, Korea"
        );

        UserInfoDto userInfoDto = new UserInfoDto(
                keycloakUserInfoDto.sub(),
                keycloakUserInfoDto.upn(),
                keycloakUserInfoDto.emailVerified(),
                "ROLE_USER",
                keycloakUserInfoDto.phoneNumber(),
                keycloakUserInfoDto.preferredUsername(),
                keycloakUserInfoDto.email(),
                keycloakUserInfoDto.customAddress()
        );

        UserSummaryDto userSummaryDto = new UserSummaryDto(1L, "ROLE_USER");
        String expectedAccessToken = "jwt-access-token";
        String expectedRefreshToken = "jwt-refresh-token";

        // Mocking
        when(keycloakAuthPort.exchangeWebViewCodeForToken(authorizationCode)).thenReturn(keycloakTokenDto);
        when(keycloakUserInfoPort.fetchUserInfo(keycloakTokenDto.accessToken())).thenReturn(keycloakUserInfoDto);
        when(registerUserPort.registerUser(any())).thenReturn(userSummaryDto);
        when(jwtTokenProvider.generateAccessToken(any(), eq(userSummaryDto))).thenReturn(expectedAccessToken);
        when(jwtTokenProvider.generateRefreshToken(userSummaryDto.userId())).thenReturn(expectedRefreshToken);

        // When
        Oauth2UserTokenResponse response = webViewLoginService.getToken(authorizationCode);

        // Then
        assertEquals(expectedAccessToken, response.accessToken());
        assertEquals(expectedRefreshToken, response.refreshToken());

        // Verify interactions
        verify(keycloakAuthPort, times(1)).exchangeWebViewCodeForToken(authorizationCode);
        verify(keycloakUserInfoPort, times(1)).fetchUserInfo("access-token");
        verify(registerUserPort, times(1)).registerUser(any());
        verify(jwtTokenProvider, times(1)).generateAccessToken(any(), eq(userSummaryDto));
        verify(jwtTokenProvider, times(1)).generateRefreshToken(userSummaryDto.userId());
    }
}
