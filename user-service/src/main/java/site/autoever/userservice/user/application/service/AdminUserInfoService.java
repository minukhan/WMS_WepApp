package site.autoever.userservice.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.autoever.userservice.infrastructure.util.UserIdResolver;
import site.autoever.userservice.user.application.dto.AdminUserInfoDto;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.exception.UserIdNotFoundException;
import site.autoever.userservice.user.application.port.in.AdminUserInfoUseCase;
import site.autoever.userservice.user.application.port.out.GetUserPort;

@Service
@RequiredArgsConstructor
public class AdminUserInfoService implements AdminUserInfoUseCase {

    private final UserIdResolver userIdResolver;
    private final GetUserPort getUserPort;

    @Override
    public AdminUserInfoDto getAdminInfo() {
        Long currentUserId = userIdResolver.getCurrentUserId();

        User user = getUserPort.getUserById(currentUserId)
                .orElseThrow(UserIdNotFoundException::new);

        return AdminUserInfoDto.of(user);
    }
}
