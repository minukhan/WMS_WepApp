package site.autoever.authservice.infrastructure.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.autoever.authservice.auth.application.dto.UserInfoDto;
import site.autoever.authservice.auth.application.dto.UserSummaryDto;
import site.autoever.authservice.auth.application.port.out.dto.TempUserInfoDto;
import site.autoever.authservice.auth.application.port.out.dto.UserDetailDto;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Key signingKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * Access Token 생성
     */
    public String generateAccessToken(UserInfoDto userInfoDto, UserSummaryDto userSummaryDto) {
        return Jwts.builder()
                .setSubject(String.valueOf(userSummaryDto.userId()))         // sub에 userId 설정
                .setIssuer("https://wherehouse.site")       // iss 설정
                .setAudience("account")                     // aud 설정
                .setIssuedAt(new Date())                    // iat 설정
                .setExpiration(
                        new Date(System.currentTimeMillis() + accessTokenExpiration)) // exp 설정
                .claim("email", userInfoDto.email())
                .claim("role", userSummaryDto.role())
                .claim("typ", "Bearer")                  // typ 추가
                .signWith(signingKey, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    /**
     * Access Token 생성
     */
    public String generateAccessToken(UserDetailDto userDetailDto) {
        return Jwts.builder()
                .setSubject(String.valueOf(userDetailDto.id()))         // sub에 userId 설정
                .setIssuer("https://wherehouse.site")       // iss 설정
                .setAudience("account")                     // aud 설정
                .setIssuedAt(new Date())                    // iat 설정
                .setExpiration(
                        new Date(System.currentTimeMillis() + accessTokenExpiration)) // exp 설정
                .claim("email", userDetailDto.email())
                .claim("role", userDetailDto.role())
                .claim("typ", "Bearer")                  // typ 추가
                .signWith(signingKey, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    /**
     * temp user access token 생성
     */
    public String generateTempUserAccessToken(TempUserInfoDto tempUserInfoDto) {
        return Jwts.builder()
                .setSubject(tempUserInfoDto.tempUserId())
                .setIssuer("https://wherehouse.site")       // iss 설정
                .setAudience("account")                     // aud 설정
                .setIssuedAt(new Date())                    // iat 설정
                .setExpiration(
                        new Date(System.currentTimeMillis() + accessTokenExpiration)) // exp 설정
                .claim("role", tempUserInfoDto.role())
                .claim("managerId", tempUserInfoDto.managerId())
                .claim("typ", "Bearer")                  // typ 추가
                .signWith(signingKey, SignatureAlgorithm.HS256) // 서명
                .compact();
    }

    /**
     * Refresh Token 생성
     */
    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // sub에 userId 설정
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Temp User Refresh Token 생성
     */
    public String generateTempUserRefreshToken(String tempUserId) {
        return Jwts.builder()
                .setSubject(tempUserId) // sub에 userId 설정
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
