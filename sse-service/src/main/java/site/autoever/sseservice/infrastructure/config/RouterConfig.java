package site.autoever.sseservice.infrastructure.config;

import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return route(GET("/actuator/info"),
                request -> ServerResponse.permanentRedirect(URI.create("/swagger-ui.html")).build());
    }
}