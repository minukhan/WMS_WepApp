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
@DisplayName("🛠️ Admin 로그인 서비스 테스트")
class AdminLoginServiceTest {

    @InjectMocks
    private AdminLoginService adminLoginService;

    @Mock
    private KeycloakAuthPort keycloakAuthPort;

    @Mock
    private KeycloakUserInfoPort keycloakUserInfoPort;

    @Mock
    private RegisterUserPort registerUserPort;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("✅ 인증 코드를 사용하여 Admin OAuth2 토큰을 성공적으로 발급한다.")
    void getToken_shouldReturnOauth2UserTokenResponse() {
        // Given
        String authorizationCode = "test-authorization-code";
        KeycloakTokenDto keycloakTokenDto = new KeycloakTokenDto("access-token", "refresh-token");

        KeycloakUserInfoDto keycloakUserInfoDto = new KeycloakUserInfoDto(
                "admin-id", "관리자", true, Set.of("ROLE_ADMIN"), "010-5678-1234",
                "관리자", "admin@example.com", "Seoul, Korea"
        );

        UserSummaryDto userSummaryDto = new UserSummaryDto(1L, "ROLE_ADMIN");
        String expectedAccessToken = "jwt-access-token";
        String expectedRefreshToken = "jwt-refresh-token";

        // Mocking
        when(keycloakAuthPort.exchangeAdminCodeForToken(authorizationCode))
                .thenReturn(keycloakTokenDto);

        when(keycloakUserInfoPort.fetchUserInfo(keycloakTokenDto.accessToken()))
                .thenReturn(keycloakUserInfoDto);

        when(registerUserPort.registerUser(any()))
                .thenReturn(userSummaryDto);

        when(jwtTokenProvider.generateAccessToken(any(), eq(userSummaryDto)))
                .thenReturn(expectedAccessToken);

        when(jwtTokenProvider.generateRefreshToken(userSummaryDto.userId()))
                .thenReturn(expectedRefreshToken);

        // When
        Oauth2UserTokenResponse response = adminLoginService.getToken(authorizationCode);

        // Then
        assertEquals(expectedAccessToken, response.accessToken());
        assertEquals(expectedRefreshToken, response.refreshToken());

        // Verify interactions
        verify(keycloakAuthPort, times(1)).exchangeAdminCodeForToken(authorizationCode);
        verify(keycloakUserInfoPort, times(1)).fetchUserInfo("access-token");
        verify(registerUserPort, times(1)).registerUser(any());
        verify(jwtTokenProvider, times(1)).generateAccessToken(any(), eq(userSummaryDto));
        verify(jwtTokenProvider, times(1)).generateRefreshToken(userSummaryDto.userId());
    }
}
