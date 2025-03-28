package site.autoever.userservice.infrastructure.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserIdResolver {
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                String principal = (String) authentication.getPrincipal();
                return Long.parseLong(principal);
            } catch (NumberFormatException e) {
                // 예외 발생 시 -1 반환
                return -1L;
            }
        }
        return null;
    }
}

