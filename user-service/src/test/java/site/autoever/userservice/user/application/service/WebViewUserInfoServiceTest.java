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
import site.autoever.userservice.user.application.dto.WebviewUserInfoDto;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.port.out.GetUserPort;

@ExtendWith(MockitoExtension.class)
class WebViewUserInfoServiceTest {

    @Mock
    private UserIdResolver userIdResolver;

    @Mock
    private GetUserPort getUserPort;

    @InjectMocks
    private WebViewUserInfoService webViewUserInfoService;

    // 헬퍼 메서드: 역할(role)에 따른 User 엔티티 생성
    private User createUser(String role) {
        return User.builder()
                .id(1L)
                .email("test@example.com")
                .address("Test Address")
                .name("Test Name")
                .phoneNumber("010-1234-5678")
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("ROLE_WAREHOUSE_MANAGER인 경우 isManager는 true")
    void whenRoleWarehouseManager_thenIsManagerTrue() {
        User user = createUser("ROLE_WAREHOUSE_MANAGER");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        WebviewUserInfoDto dto = webViewUserInfoService.getWebViewInfo();

        assertTrue(dto.isManager(), "ROLE_WAREHOUSE_MANAGER이면 isManager는 true여야 합니다.");
    }

    @Test
    @DisplayName("ROLE_USER인 경우 isManager는 false")
    void whenRoleUser_thenIsManagerFalse() {
        User user = createUser("ROLE_USER");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        WebviewUserInfoDto dto = webViewUserInfoService.getWebViewInfo();

        assertFalse(dto.isManager(), "ROLE_USER이면 isManager는 false여야 합니다.");
    }

    @Test
    @DisplayName("ROLE_VERIFIED_USER인 경우 isManager는 false")
    void whenRoleVerifiedUser_thenIsManagerFalse() {
        User user = createUser("ROLE_VERIFIED_USER");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        WebviewUserInfoDto dto = webViewUserInfoService.getWebViewInfo();

        assertFalse(dto.isManager(), "ROLE_VERIFIED_USER이면 isManager는 false여야 합니다.");
    }

    @Test
    @DisplayName("ROLE_INFRA_MANAGER인 경우 isManager는 false")
    void whenRoleInfraManager_thenIsManagerFalse() {
        User user = createUser("ROLE_INFRA_MANAGER");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        WebviewUserInfoDto dto = webViewUserInfoService.getWebViewInfo();

        assertFalse(dto.isManager(), "ROLE_INFRA_MANAGER이면 isManager는 false여야 합니다.");
    }

    @Test
    @DisplayName("ROLE_ADMIN인 경우 isManager는 true")
    void whenRoleAdmin_thenIsManagerTrue() {
        User user = createUser("ROLE_ADMIN");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        WebviewUserInfoDto dto = webViewUserInfoService.getWebViewInfo();

        assertTrue(dto.isManager(), "ROLE_ADMIN이면 isManager는 true여야 합니다.");
    }
}
