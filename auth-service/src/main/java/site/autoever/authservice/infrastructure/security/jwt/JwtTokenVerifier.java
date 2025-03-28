package site.autoever.authservice.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
                    .parseClaimsJws(token)     // 토큰 검증 및 디코딩
                    .getBody();                // JWT의 클레임 반환
        } catch (SecurityException e) {
            throw new RuntimeException("Invalid JWT signature", e);
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("JWT token is expired", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Invalid JWT token format", e);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * RefreshToken에서 sub 클레임을 추출하는 메서드
     * @param refreshToken 검증할 리프레시 토큰
     * @return sub (사용자 ID)
     */
    public String getSubjectFromRefreshToken(String refreshToken) {
        try {
            Claims claims = verifyToken(refreshToken);
            return claims.getSubject(); // sub 클레임 반환
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract subject from RefreshToken", e);
        }
    }
}
