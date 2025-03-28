package hyundai.purchaseservice.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.ErrorDecoder;
import hyundai.purchaseservice.common.exception.FeignCustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig implements RequestInterceptor {
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignCustomException();
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String userId = request.getHeader("X-User-Id");
            String role = request.getHeader("X-User-Role");
            if (userId != null) {
                requestTemplate.header("X-User-Id", userId);
            }
            if (role != null) {
                requestTemplate.header("X-User-Role", role);
            }
        }
    }
}