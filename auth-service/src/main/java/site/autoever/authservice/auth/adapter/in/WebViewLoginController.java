package site.autoever.authservice.auth.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.autoever.authservice.auth.adapter.in.dto.AuthorizationCodeRequest;
import site.autoever.authservice.auth.adapter.in.dto.Oauth2UserTokenResponse;
import site.autoever.authservice.auth.application.port.in.WebViewLoginUseCase;

@RestController
@RequiredArgsConstructor
@Tag(name = "웹뷰 로그인 API", description = "웹뷰 경로로 OAuth2 인증 및 토큰 발급 API")
public class WebViewLoginController {
    private final WebViewLoginUseCase webViewLoginUseCase;

    @Operation(summary = "OAuth2 토큰 발급", description = "Authorization 코드를 통해 OAuth2 Access Token과 Refresh Token을 발급합니다.")
    @PostMapping("/auth/web-view/login")
    public ResponseEntity<?> generateToken(
            @Parameter(description = "Authorization 코드 요청 객체", required = true)
            @RequestBody AuthorizationCodeRequest request) {
        Oauth2UserTokenResponse tokenResponse = webViewLoginUseCase.getToken(request.authorizationCode());

        return ResponseEntity.ok(tokenResponse);
    }
}
