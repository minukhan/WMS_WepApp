package site.autoever.eurekaclient.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.autoever.eurekaclient.infrastructure.security.exception.JwtAccessTokenExpireException;

import javax.crypto.SecretKey;

@Component
public class JwtTokenVerifier {

    private final SecretKey signingKey;

    public JwtTokenVerifier(@Value("${jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims verifyToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey) // 서명 검증에 사용할 키 설정
                    .build()
                    .parseClaimsJws(token) // 토큰 검증 및 디코딩
                    .getBody(); // JWT의 클레임 반환
        } catch (SecurityException e) {
            throw new RuntimeException("Invalid JWT signature", e);
        } catch (ExpiredJwtException e) {
            throw new JwtAccessTokenExpireException("JWT token is expired");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid JWT token format", e);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
}
