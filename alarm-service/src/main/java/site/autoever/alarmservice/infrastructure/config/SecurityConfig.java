package site.autoever.alarmservice.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import site.autoever.alarmservice.infrastructure.security.filter.SecurityContextWebFilter;

@Configuration
@RequiredArgsConstructor
@EnableReactiveMethodSecurity  // reactive method security 활성화
@EnableWebFluxSecurity
public class SecurityConfig {

    private final SecurityContextWebFilter securityContextWebFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange.anyExchange().permitAll()) // 모든 요청 허용
                .addFilterAt(securityContextWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
