package site.autoever.authservice.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (필요에 따라)
                .csrf(AbstractHttpConfigurer::disable)
                // 모든 경로에 대해 인증 없이 접근하도록 설정
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                // 기본 폼 로그인 기능 비활성화
                .formLogin(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
