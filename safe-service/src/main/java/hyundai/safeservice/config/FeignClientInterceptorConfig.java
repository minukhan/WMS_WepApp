package hyundai.safeservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Component
public class FeignClientInterceptorConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String userId = request.getHeader("X-User-Id");
            String role = request.getHeader("X-User-Role");

            if (userId != null) {
                requestTemplate.header("X-User-Id", userId);
            } else {
                requestTemplate.header("X-User-Id","1");  // 기본값 추가
            }

            if (role != null) {
                requestTemplate.header("X-User-Role", role);
            } else {
                requestTemplate.header("X-User-Role", "ROLE_ADMIN");  // 기본값 추가
            }
        } else {
            // HTTP 요청 컨텍스트가 없을 경우 (ex: 스케줄러 실행 시) 기본값 설정
            requestTemplate.header("X-User-Id", "admin");
            requestTemplate.header("X-User-Role", "ROLE_ADMIN");
        }
    }
}
