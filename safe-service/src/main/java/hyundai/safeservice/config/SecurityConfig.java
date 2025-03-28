package hyundai.safeservice.config;



import hyundai.safeservice.security.filter.SecurityContextFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // spring 클래스임을 명시
@EnableWebSecurity // Spring security 를 활성화하는
@EnableMethodSecurity// @PreAuthorize, @PostAuthorize, @Secured 등의 애너테이션을 사용가능
public class SecurityConfig {


    // 생성자 주입을 통해 securityContextFilter 주입
    private final SecurityContextFilter securityContextFilter;

    public SecurityConfig(SecurityContextFilter securityContextFilter) {
        this.securityContextFilter = securityContextFilter;
    }


    // Spring Container에 Bean 등록
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable) // 크로스 사이트 요청 위조 보호 해제
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll())  // HTTP 모든 요청 허용
                .addFilterBefore(securityContextFilter, UsernamePasswordAuthenticationFilter.class) // securityContextFilter를 먼저 실행. 커스텀 필터
                .build();
    }

}