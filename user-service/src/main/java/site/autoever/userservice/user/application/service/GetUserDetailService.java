package site.autoever.userservice.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.autoever.userservice.infrastructure.util.UserIdResolver;
import site.autoever.userservice.user.adapter.in.dto.UserDetailResponse;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.exception.UserIdNotFoundException;
import site.autoever.userservice.user.application.port.in.GetUserDetailUseCase;
import site.autoever.userservice.user.application.port.out.GetUserPort;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetUserDetailService implements GetUserDetailUseCase {

    private final GetUserPort getUserPort;
    private final UserIdResolver userIdResolver;

    @Override
    public UserDetailResponse getUserDetail(long userId) {
        Long currentUserId = userIdResolver.getCurrentUserId();

        // currentUserId가 -1이면 anonymous 처리
        String currentUserRole;
        User currentUser = null;

        if (currentUserId != null && currentUserId != -1) {
            currentUser = getUserPort.getUserById(currentUserId)
                    .orElse(null);
            currentUserRole = (currentUser != null) ? currentUser.getRole() : "anonymous";
        } else {
            currentUserRole = "anonymous";
        }

        log.info("Current User Role: {}", currentUserRole);

        User targetUser = getUserPort.getUserById(userId)
                .orElseThrow(UserIdNotFoundException::new);
        log.info("Target User: {}", targetUser.toString());

        return UserDetailResponse.from(currentUserRole, targetUser);
    }

}
