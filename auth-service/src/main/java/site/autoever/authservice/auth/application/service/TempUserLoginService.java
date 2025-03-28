package site.autoever.authservice.auth.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.autoever.authservice.auth.adapter.in.dto.Oauth2UserTokenResponse;
import site.autoever.authservice.auth.application.port.in.TempUserLoginUseCase;
import site.autoever.authservice.auth.application.port.out.RegisterUserPort;
import site.autoever.authservice.auth.application.port.out.dto.TempUserInfoDto;
import site.autoever.authservice.auth.application.port.out.dto.TempUserRegisterRequest;
import site.autoever.authservice.infrastructure.security.jwt.JwtTokenProvider;
import site.autoever.authservice.infrastructure.util.RedisUtil;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TempUserLoginService implements TempUserLoginUseCase {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;
    private final RegisterUserPort registerUserPort;

    public Oauth2UserTokenResponse getToken(Long managerId) {
        String tempUserId = UUID.randomUUID().toString();

        log.info("tempUserId = {}, managerId = {}", tempUserId, managerId);

        redisUtil.setDataExpireOneDay(tempUserId, managerId);

        //fegin 요청
        TempUserInfoDto tempUserInfoDto = registerUserPort.registerTempUser(new TempUserRegisterRequest(tempUserId, managerId));

        String accessToken = jwtTokenProvider.generateTempUserAccessToken(tempUserInfoDto);

        String refreshToken = jwtTokenProvider.generateTempUserRefreshToken(tempUserId);

        return Oauth2UserTokenResponse.of(accessToken, refreshToken);
    }
}
