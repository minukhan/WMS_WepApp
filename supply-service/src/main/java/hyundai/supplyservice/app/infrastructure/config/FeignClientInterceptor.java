package hyundai.supplyservice.app.infrastructure.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FeignClientInterceptor implements RequestInterceptor {
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
