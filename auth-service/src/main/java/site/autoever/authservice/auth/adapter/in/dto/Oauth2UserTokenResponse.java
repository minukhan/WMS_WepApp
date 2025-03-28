package site.autoever.authservice.auth.adapter.in.dto;

public record Oauth2UserTokenResponse(
        String accessToken,
        String refreshToken
) {
    public static Oauth2UserTokenResponse of(String accessToken, String refreshToken) {
        return new Oauth2UserTokenResponse(accessToken, refreshToken);
    }
}
