package site.autoever.authservice.auth.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.authservice.auth.adapter.in.dto.Oauth2UserTokenResponse;
import site.autoever.authservice.auth.adapter.in.dto.RefreshTokenRequest;
import site.autoever.authservice.auth.application.port.in.ReissueTokenUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "토큰 재발급 API", description = "리프레시 토큰으로 토큰 재발급 API")
public class ReissueTokenController {
    private final ReissueTokenUseCase reissueTokenUseCase;

    @Operation(summary = "OAuth2 토큰 재발급", description = "리프레시 토큰을 통해 OAuth2 Access Token과 Refresh Token을 발급합니다.")
    @PostMapping("/auth/reissue")
    public ResponseEntity<?> generateToken(
            @Parameter(description = "refreshToken 요청 객체", required = true)
            @RequestBody RefreshTokenRequest request) {
        Oauth2UserTokenResponse tokenResponse = reissueTokenUseCase.reissueToken(request.refreshToken());

        return ResponseEntity.ok(tokenResponse);
    }
}
