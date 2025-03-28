package site.autoever.userservice.user.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.autoever.userservice.infrastructure.util.UserIdResolver;
import site.autoever.userservice.user.adapter.in.dto.ChangeUserRoleRequest;
import site.autoever.userservice.user.adapter.in.dto.UserDetailResponse;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.exception.UserIdNotFoundException;
import site.autoever.userservice.user.application.port.in.ChangeUserRoleUseCase;
import site.autoever.userservice.user.application.port.out.GetUserPort;
import site.autoever.userservice.user.application.port.out.RegisterUserPort;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChangeUserRoleService implements ChangeUserRoleUseCase {

    private final UserIdResolver userIdResolver;
    private final GetUserPort getUserPort;
    private final RegisterUserPort registerUserPort;

    @Transactional
    @Override
    public UserDetailResponse modifyUser(ChangeUserRoleRequest request) {
        User targetUser = getUserPort.getUserById(request.userId())
                .orElseThrow(UserIdNotFoundException::new);

        User currentUser = getUserPort.getUserById(userIdResolver.getCurrentUserId())
                .orElseThrow(UserIdNotFoundException::new);

        log.info("현재 요청하는 유저 : {}, 타겟 유저 : {}", currentUser.toString(), targetUser.toString());

        User modifiedUser = User.builder()
                .id(targetUser.getId())
                .name(targetUser.getName())
                .email(targetUser.getEmail())
                .address(targetUser.getAddress())
                .phoneNumber(targetUser.getPhoneNumber())
                .createdAt(targetUser.getCreatedAt())
                .role(request.role())
                .build();

        log.info("역할 변경된 유저 : {}", modifiedUser.toString());

        return UserDetailResponse.from(currentUser.getRole(), registerUserPort.saveUser(modifiedUser));
    }
}
