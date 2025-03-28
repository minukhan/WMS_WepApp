package site.autoever.authservice.infrastructure.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private long expiration; // AccessToken 유효기간 (ms)
    private long refreshTokenExpiration; // RefreshToken 유효기간 (ms)
}