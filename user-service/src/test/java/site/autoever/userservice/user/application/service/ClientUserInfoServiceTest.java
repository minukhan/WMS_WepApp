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
import site.autoever.userservice.user.application.dto.ClientUserInfoDto;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.exception.UserIdNotFoundException;
import site.autoever.userservice.user.application.port.out.GetUserPort;

@ExtendWith(MockitoExtension.class)
class ClientUserInfoServiceTest {

    @Mock
    private UserIdResolver userIdResolver;

    @Mock
    private GetUserPort getUserPort;

    @InjectMocks
    private ClientUserInfoService clientUserInfoService;

    // 헬퍼 메서드: 역할(role)에 따른 User 엔티티 생성
    private User createUser(String role) {
        return User.builder()
                .id(1L)
                .email("client@example.com")
                .address("Client Address")
                .name("Client Name")
                .phoneNumber("010-1111-2222")
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("ROLE_USER인 경우 verified는 false")
    void whenRoleUser_thenVerifiedFalse() {
        User user = createUser("ROLE_USER");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        ClientUserInfoDto dto = clientUserInfoService.getClientInfo();

        assertFalse(dto.verified(), "ROLE_USER이면 verified는 false여야 합니다.");
    }

    @Test
    @DisplayName("ROLE_WAREHOUSE_MANAGER인 경우 verified는 true")
    void whenRoleWarehouseManager_thenVerifiedTrue() {
        User user = createUser("ROLE_WAREHOUSE_MANAGER");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        ClientUserInfoDto dto = clientUserInfoService.getClientInfo();

        assertTrue(dto.verified(), "ROLE_WAREHOUSE_MANAGER이면 verified는 true여야 합니다.");
    }

    @Test
    @DisplayName("ROLE_VERIFIED_USER인 경우 verified는 true")
    void whenRoleVerifiedUser_thenVerifiedTrue() {
        User user = createUser("ROLE_VERIFIED_USER");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        ClientUserInfoDto dto = clientUserInfoService.getClientInfo();

        assertTrue(dto.verified(), "ROLE_VERIFIED_USER이면 verified는 true여야 합니다.");
    }

    @Test
    @DisplayName("ROLE_INFRA_MANAGER인 경우 verified는 true")
    void whenRoleInfraManager_thenVerifiedTrue() {
        User user = createUser("ROLE_INFRA_MANAGER");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        ClientUserInfoDto dto = clientUserInfoService.getClientInfo();

        assertTrue(dto.verified(), "ROLE_INFRA_MANAGER이면 verified는 true여야 합니다.");
    }

    @Test
    @DisplayName("ROLE_ADMIN인 경우 verified는 true")
    void whenRoleAdmin_thenVerifiedTrue() {
        User user = createUser("ROLE_ADMIN");
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.of(user));

        ClientUserInfoDto dto = clientUserInfoService.getClientInfo();

        assertTrue(dto.verified(), "ROLE_ADMIN이면 verified는 true여야 합니다.");
    }

    @Test
    @DisplayName("사용자 ID가 존재하지 않으면 UserIdNotFoundException 발생")
    void whenUserNotFound_thenThrowException() {
        when(userIdResolver.getCurrentUserId()).thenReturn(1L);
        when(getUserPort.getUserById(1L)).thenReturn(Optional.empty());

        assertThrows(UserIdNotFoundException.class, () -> clientUserInfoService.getClientInfo());
    }
}
