package site.autoever.authservice.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String TITLE = "인증 서비스 API 명세";
    private static final String VERSION = "0.1";
    private static final String DESCRIPTION = "WMS 프로젝트 백엔드 API 명세서입니다.";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(TITLE)
                        .version(VERSION)
                        .description(DESCRIPTION))
                // 서버 URL에 /api 추가
                .servers(List.of(
                        new Server().url("/").description("WMS Auth API Base URL")
                ));
    }
}
