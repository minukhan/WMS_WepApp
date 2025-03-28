package site.autoever.userservice.user.application.service;

import static org.junit.jupiter.api.Assertions.*;
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
import site.autoever.userservice.user.application.dto.AdminUserInfoDto;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.exception.UserIdNotFoundException;
import site.autoever.userservice.user.application.port.out.GetUserPort;

@ExtendWith(MockitoExtension.class)
class AdminUserInfoServiceTest {

    @Mock
    private UserIdResolver userIdResolver;

    @Mock
    private GetUserPort getUserPort;

    @InjectMocks
    private AdminUserInfoService adminUserInfoService;

    // 헬퍼 메서드: 주어진 역할에 따른 User 엔티티 생성
    private User createUser(String role) {
        return User.builder()
                .id(1L)
                .email("admin@example.com")
                .address("Admin Address")
                .name("Admin Name")
                .phoneNumber("010-0000-0000")
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("ROLE_USER인 경우 isAdmin는 false")
    void whenRoleUser_thenIsAdminFalse() {
        User user = createUser("ROLE_USER");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        AdminUserInfoDto dto = adminUserInfoService.getAdminInfo();
        assertFalse(dto.isAdmin(), "ROLE_USER이면 isAdmin는 false여야 합니다.");
    }

    @Test
    @DisplayName("ROLE_WAREHOUSE_MANAGER인 경우 isAdmin는 false")
    void whenRoleWarehouseManager_thenIsAdminFalse() {
        User user = createUser("ROLE_WAREHOUSE_MANAGER");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        AdminUserInfoDto dto = adminUserInfoService.getAdminInfo();
        assertFalse(dto.isAdmin(), "ROLE_WAREHOUSE_MANAGER이면 isAdmin는 false여야 합니다.");
    }

    @Test
    @DisplayName("ROLE_VERIFIED_USER인 경우 isAdmin는 false")
    void whenRoleVerifiedUser_thenIsAdminFalse() {
        User user = createUser("ROLE_VERIFIED_USER");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        AdminUserInfoDto dto = adminUserInfoService.getAdminInfo();
        assertFalse(dto.isAdmin(), "ROLE_VERIFIED_USER이면 isAdmin는 false여야 합니다.");
    }

    @Test
    @DisplayName("ROLE_INFRA_MANAGER인 경우 isAdmin는 false")
    void whenRoleInfraManager_thenIsAdminFalse() {
        User user = createUser("ROLE_INFRA_MANAGER");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        AdminUserInfoDto dto = adminUserInfoService.getAdminInfo();
        assertFalse(dto.isAdmin(), "ROLE_INFRA_MANAGER이면 isAdmin는 false여야 합니다.");
    }

    @Test
    @DisplayName("ROLE_ADMIN인 경우 isAdmin는 true")
    void whenRoleAdmin_thenIsAdminTrue() {
        User user = createUser("ROLE_ADMIN");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        AdminUserInfoDto dto = adminUserInfoService.getAdminInfo();
        assertTrue(dto.isAdmin(), "ROLE_ADMIN이면 isAdmin는 true여야 합니다.");
    }

    @Test
    @DisplayName("사용자 ID가 존재하지 않으면 UserIdNotFoundException 발생")
    void whenUserNotFound_thenThrowException() {
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.empty());
        assertThrows(UserIdNotFoundException.class, () -> adminUserInfoService.getAdminInfo());
    }
}
