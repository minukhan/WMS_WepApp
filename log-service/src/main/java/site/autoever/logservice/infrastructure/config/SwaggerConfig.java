package site.autoever.logservice.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("로그 서비스 API 명세")
                        .version("0.1")
                        .description("WMS 프로젝트 백엔드 API 명세서입니다."))
                .components(new Components()
                        // X-User-Id Security Scheme 추가
                        .addSecuritySchemes("X-User-Id-Scheme", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-User-Id")
                        )
                        // X-User-Role Security Scheme 추가
                        .addSecuritySchemes("X-User-Role-Scheme", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-User-Role")
                        )
                )
                // 두 개의 Security Requirement 추가
                .addSecurityItem(new SecurityRequirement()
                        .addList("X-User-Id-Scheme")
                        .addList("X-User-Role-Scheme")
                )
                .servers(List.of(
                        new Server().url("/").description("WMS Part API Base URL")
                ));
    }
}
