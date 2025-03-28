package site.autoever.eurekaclient.infrastructure.security.exception;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtAccessTokenExpireException extends RuntimeException {
    public JwtAccessTokenExpireException(String message) {
    }
}
