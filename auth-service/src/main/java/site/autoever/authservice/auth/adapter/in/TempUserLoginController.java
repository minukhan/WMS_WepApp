package site.autoever.authservice.auth.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.authservice.auth.adapter.in.dto.CreateTempAuthRequest;
import site.autoever.authservice.auth.adapter.in.dto.Oauth2UserTokenResponse;
import site.autoever.authservice.auth.application.port.in.TempUserLoginUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "임시 유저 (현장 근로자) 로그인 API", description = "임시 유저 OAuth2 인증 및 토큰 발급 API")
public class TempUserLoginController {

    private final TempUserLoginUseCase tempUserLoginUseCase;

    @Operation(summary = "OAuth2 토큰 발급", description = "temp USER ID를 통해 OAuth2 Access Token과 Refresh Token을 발급합니다.")
    @PostMapping("/auth/temp-user/login")
    public ResponseEntity<?> generateToken(
            @Parameter(description = "tempUserId 요청 객체", required = true)
            @RequestBody CreateTempAuthRequest request) {
        Oauth2UserTokenResponse tokenResponse = tempUserLoginUseCase.getToken(request.managerId());

        return ResponseEntity.ok(tokenResponse);
    }

}
