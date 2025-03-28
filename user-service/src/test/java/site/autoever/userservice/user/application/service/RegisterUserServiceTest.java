package site.autoever.userservice.user.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.autoever.userservice.user.application.dto.UserDetailDto;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.port.out.GetUserPort;
import site.autoever.userservice.user.application.port.out.RegisterUserPort;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserServiceTest {

    @Mock
    private RegisterUserPort registerUserPort;

    @Mock
    private GetUserPort getUserPort;

    @InjectMocks
    private RegisterUserService registerUserService;

    @Test
    @DisplayName("새로운 유저가 생성되지 않고, 기존 유저의 id가 반환되며 saveUser가 호출되지 않는다.")
    void testRegisterUser_whenUserAlreadyExists() {
        // given
        User requestUser = User.builder()
                .email("existing@example.com")
                .build();

        User existingUser = User.builder()
                .id(1L)
                .email("existing@example.com")
                .build();

        UserDetailDto expectedResult = UserDetailDto.of(existingUser);

        // when
        when(getUserPort.getUserByEmail(requestUser.getEmail()))
                .thenReturn(Optional.of(existingUser));

        // 실행
        UserDetailDto result = registerUserService.registerUser(requestUser);

        // then (검증)
        assertEquals(expectedResult, result);                         // 반환된 ID 검증
        verify(registerUserPort, never()).saveUser(any()); // saveUser 호출 여부 검증
    }

    @Test
    @DisplayName("새로운 유저가 정상적으로 생성된다.")
    void testRegisterUser_whenUserDoesNotExist() {
        // given
        User requestUser = User.builder()
                .email("new-user@example.com")
                .build();

        User expectedUser = User.builder()
                .id(1L)
                .email("new-user@example.com")
                .build();

        UserDetailDto expectedResult = UserDetailDto.of(expectedUser);

        // when
        when(getUserPort.getUserByEmail(requestUser.getEmail()))
                .thenReturn(Optional.empty());

        when(registerUserPort.saveUser(requestUser))
                .thenReturn(expectedUser);

        UserDetailDto result = registerUserService.registerUser(requestUser);

        // then
        assertEquals(expectedResult, result);
        verify(registerUserPort, times(1)).saveUser(requestUser);  // saveUser가 한 번 호출됨
    }
}
