package site.autoever.authservice.auth.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.autoever.authservice.auth.adapter.in.dto.Oauth2UserTokenResponse;
import site.autoever.authservice.auth.application.dto.UserSummaryDto;
import site.autoever.authservice.auth.application.port.in.ReissueTokenUseCase;
import site.autoever.authservice.auth.application.port.out.RegisterUserPort;
import site.autoever.authservice.auth.application.port.out.dto.UserDetailDto;
import site.autoever.authservice.infrastructure.security.jwt.JwtTokenProvider;
import site.autoever.authservice.infrastructure.security.jwt.JwtTokenVerifier;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReissueTokenService implements ReissueTokenUseCase {

    private final RegisterUserPort registerUserPort;
    private final JwtTokenVerifier jwtTokenVerifier;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Oauth2UserTokenResponse reissueToken(String refreshToken) {
        Long userId = Long.valueOf(jwtTokenVerifier.getSubjectFromRefreshToken(refreshToken));
        log.info("refreshToken에서 추출한 userId : {}", userId);

        UserDetailDto userDetailDto = registerUserPort.findUser(userId);

        String accessToken = jwtTokenProvider.generateAccessToken(userDetailDto);

        return Oauth2UserTokenResponse.of(accessToken, refreshToken);
    }
}
