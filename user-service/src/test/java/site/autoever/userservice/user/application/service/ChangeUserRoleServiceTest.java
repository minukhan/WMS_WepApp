package site.autoever.userservice.user.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.autoever.userservice.infrastructure.util.UserIdResolver;
import site.autoever.userservice.user.adapter.in.dto.ChangeUserRoleRequest;
import site.autoever.userservice.user.adapter.in.dto.UserDetailResponse;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.port.out.GetUserPort;
import site.autoever.userservice.user.application.port.out.RegisterUserPort;

@ExtendWith(MockitoExtension.class)
class ChangeUserRoleServiceTest {

    @Mock
    private UserIdResolver userIdResolver;

    @Mock
    private GetUserPort getUserPort;

    @Mock
    private RegisterUserPort registerUserPort;

    @InjectMocks
    private ChangeUserRoleService changeUserRoleService;

    @DisplayName("유저 역할이 변경되었을 경우 (기존 역할과 다른 역할)")
    @Test
    void modifyUser_roleChanged() {
        // given
        Long targetUserId = 1L;
        Long currentUserId = 2L;
        // 요청으로 전달된 역할은 기존 역할("USER")과 다르게 "ADMIN"으로 변경 요청
        ChangeUserRoleRequest request = new ChangeUserRoleRequest(targetUserId, "ROLE_ADMIN");

        // 대상 유저는 기존에 "USER" 역할을 가지고 있음
        User targetUser = User.builder()
                .id(targetUserId)
                .name("Target User")
                .email("target@example.com")
                .address("Seoul")
                .phoneNumber("010-1111-2222")
                .createdAt(LocalDateTime.now())
                .role("ROLE_USER")
                .build();

        // 현재 요청을 수행하는 유저 (예시로 currentUserId 사용)
        User currentUser = User.builder()
                .id(currentUserId)
                .name("Current User")
                .email("current@example.com")
                .address("Busan")
                .phoneNumber("010-3333-4444")
                .createdAt(LocalDateTime.now())
                .role("ROLE_USER")
                .build();

        // stubbing
        when(getUserPort.getUserById(targetUserId)).thenReturn(Optional.of(targetUser));
        when(userIdResolver.getCurrentUserId()).thenReturn(currentUserId);
        when(getUserPort.getUserById(currentUserId)).thenReturn(Optional.of(currentUser));
        // saveUser 호출 시, 전달된 modifiedUser를 그대로 반환하도록 함
        when(registerUserPort.saveUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserDetailResponse response = changeUserRoleService.modifyUser(request);

        // then
        assertNotNull(response);
        // 응답에 포함된 대상 유저의 역할이 "ADMIN"으로 변경되었음을 확인
        assertEquals("ROLE_ADMIN", response.role());
        // 대상 유저의 다른 필드가 변경되지 않았는지도 검증
        assertEquals(targetUser.getId(), response.id());
        assertEquals(targetUser.getName(), response.name());
    }

    @DisplayName("유저 역할이 변경되지 않은 경우 (요청 역할과 기존 역할이 동일한 경우)")
    @Test
    void modifyUser_roleNotChanged() {
        // given
        Long targetUserId = 1L;
        Long currentUserId = 2L;
        // 요청으로 전달된 역할이 기존 역할("USER")과 동일
        ChangeUserRoleRequest request = new ChangeUserRoleRequest(targetUserId, "ROLE_USER");

        // 대상 유저의 기존 역할은 "USER"
        User targetUser = User.builder()
                .id(targetUserId)
                .name("Target User")
                .email("target@example.com")
                .address("Seoul")
                .phoneNumber("010-1111-2222")
                .createdAt(LocalDateTime.now())
                .role("ROLE_USER")
                .build();

        // 현재 요청을 수행하는 유저
        User currentUser = User.builder()
                .id(currentUserId)
                .name("Current User")
                .email("current@example.com")
                .address("Busan")
                .phoneNumber("010-3333-4444")
                .createdAt(LocalDateTime.now())
                .role("ROLE_ADMIN")
                .build();

        // stubbing
        when(getUserPort.getUserById(targetUserId)).thenReturn(Optional.of(targetUser));
        when(userIdResolver.getCurrentUserId()).thenReturn(currentUserId);
        when(getUserPort.getUserById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(registerUserPort.saveUser(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        UserDetailResponse response = changeUserRoleService.modifyUser(request);

        // then
        assertNotNull(response);
        // 대상 유저의 역할이 변경되지 않고 "USER"로 남아있음을 확인
        assertEquals("ROLE_USER", response.role());
        assertEquals(targetUser.getId(), response.id());
        assertEquals(targetUser.getName(), response.name());
    }
}
