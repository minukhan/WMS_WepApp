package site.autoever.authservice.auth.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.autoever.authservice.auth.adapter.in.dto.Oauth2UserTokenResponse;
import site.autoever.authservice.auth.application.dto.KeycloakTokenDto;
import site.autoever.authservice.auth.application.dto.KeycloakUserInfoDto;
import site.autoever.authservice.auth.application.dto.UserInfoDto;
import site.autoever.authservice.auth.application.dto.UserSummaryDto;
import site.autoever.authservice.auth.application.port.in.AdminLoginUseCase;
import site.autoever.authservice.auth.application.port.out.KeycloakAuthPort;
import site.autoever.authservice.auth.application.port.out.KeycloakUserInfoPort;
import site.autoever.authservice.auth.application.port.out.RegisterUserPort;
import site.autoever.authservice.infrastructure.security.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminLoginService implements AdminLoginUseCase {

    private final KeycloakAuthPort keycloakAuthPort;
    private final KeycloakUserInfoPort keycloakUserInfoPort;
    private final RegisterUserPort registerUserPort;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Oauth2UserTokenResponse getToken(String authorizationCode) {
        // 1. authorization code로 key cloak access token 요청
        KeycloakTokenDto keycloakTokenDto = keycloakAuthPort.exchangeAdminCodeForToken(authorizationCode);

        // 2. access token으로 user info 조회
        KeycloakUserInfoDto keycloakUserInfoDto = keycloakUserInfoPort.fetchUserInfo(keycloakTokenDto.accessToken());

        // 3. keycloak user info dto를 파싱하여 유저 생성 request 생성
        UserInfoDto userInfoDto = mapToUserInfoDto(keycloakUserInfoDto);

        // 4. 해당 정보를 바탕으로 user service와 서비스 동기 통신 수행
        UserSummaryDto userSummaryDto = registerUserPort.registerUser(userInfoDto);

        // 5. access, refresh token 생성
        String accessToken = jwtTokenProvider.generateAccessToken(userInfoDto, userSummaryDto);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userSummaryDto.userId());

        // 6. oauth2 토큰 리턴
        return Oauth2UserTokenResponse.of(accessToken, refreshToken);
    }

    private UserInfoDto mapToUserInfoDto(KeycloakUserInfoDto keycloakUserInfoDto) {
        String role = keycloakUserInfoDto.groups().contains("ROLE_ADMIN") ? "ROLE_ADMIN" : "ROLE_USER";

        return new UserInfoDto(
                keycloakUserInfoDto.sub(),
                keycloakUserInfoDto.upn(),
                keycloakUserInfoDto.emailVerified(),
                role,
                keycloakUserInfoDto.phoneNumber(),
                keycloakUserInfoDto.preferredUsername(),
                keycloakUserInfoDto.email(),
                keycloakUserInfoDto.customAddress()
        );
    }

}
