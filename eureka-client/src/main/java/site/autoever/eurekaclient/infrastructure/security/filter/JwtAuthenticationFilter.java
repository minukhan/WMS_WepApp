package site.autoever.eurekaclient.infrastructure.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import site.autoever.eurekaclient.infrastructure.security.exception.JwtAccessTokenExpireException;
import site.autoever.eurekaclient.infrastructure.security.jwt.JwtTokenVerifier;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtTokenVerifier jwtTokenVerifier;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = extractToken(request);

        if (token != null) {
            try {
                Claims claims = jwtTokenVerifier.verifyToken(token);
                String userId = claims.getSubject();
                String role = claims.get("role", String.class);

                ServerHttpRequest mutatedRequest = request.mutate()
                        .header("X-User-Id", userId)
                        .header("X-User-Role", role)
                        .build();
                ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userId, null, Collections.singletonList(new SimpleGrantedAuthority(role))
                );
                SecurityContext context = new SecurityContextImpl(authentication);

                log.info("✅ Authenticated User - ID: {}, Role: {}", userId, role);

                return chain.filter(mutatedExchange)
                        .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context)));

            } catch (ExpiredJwtException | JwtAccessTokenExpireException e) {
                log.error("❌ Expired JWT token: {}", e.getMessage());
                return handleError(exchange, HttpStatus.UNAUTHORIZED, "Expired JWT token");
            } catch (MalformedJwtException e) {
                log.error("❌ Malformed JWT token: {}", e.getMessage());
                return handleError(exchange, HttpStatus.BAD_REQUEST, "Malformed JWT token");
            } catch (Exception e) {
                log.error("❌ Invalid JWT token: {}", e.getMessage());
                return handleError(exchange, HttpStatus.UNAUTHORIZED, "Invalid JWT token");
            }
        }

        log.debug("⚠️ No JWT token found in request.");
        return chain.filter(exchange);
    }


    /**
     * 에러 응답 작성 메서드
     */
    private Mono<Void> handleError(ServerWebExchange exchange, HttpStatus status, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 요청의 Origin 헤더가 허용 목록에 있으면 응답 헤더에 Access-Control-Allow-Origin 추가
        String origin = exchange.getRequest().getHeaders().getOrigin();
        if (origin != null && (
                origin.equals("http://localhost:3000") ||
                        origin.equals("https://wherehouse.site") ||
                        origin.equals("http://127.0.0.1:5500")
        )) {
            response.getHeaders().set("Access-Control-Allow-Origin", origin);
        }

        String errorResponse = String.format("{\"error\":\"%s\",\"message\":\"%s\"}", status, message);
        byte[] bytes = errorResponse.getBytes(StandardCharsets.UTF_8);

        // Content-Length 명시
        response.getHeaders().setContentLength(bytes.length);

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }



    /**
     * 쿠키 "accessToken"이 있으면 사용, 없으면 Authorization 헤더에서 추출
     */
    private String extractToken(ServerHttpRequest request) {
        // 1. 쿠키에서 토큰 추출
        Optional<String> cookieToken = Optional.ofNullable(request.getCookies().getFirst("accessToken"))
                .map(HttpCookie::getValue);
        if (cookieToken.isPresent()) {
            log.info("JWT 토큰을 쿠키에서 추출함.");
            return cookieToken.get();
        }

        // 2. Authorization 헤더 확인
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            log.info("JWT 토큰을 Authorization 헤더에서 추출함.");
            return bearerToken.substring(7);
        }
        return null;
    }
}
