package hyundai.purchaseservice.infrastructure.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserIdResolver {
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // principal이 userId로 설정되어 있으므로 이를 반환
            String principal = (String) authentication.getPrincipal();
            return Long.parseLong(principal);
        }
        return null;
    }
}
