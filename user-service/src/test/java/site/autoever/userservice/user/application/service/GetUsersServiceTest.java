package site.autoever.userservice.user.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.autoever.userservice.user.application.dto.UserInfoDto;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.port.out.GetUserPort;

@ExtendWith(MockitoExtension.class)
class GetUsersServiceTest {

    @Mock
    private GetUserPort getUserPort;

    @InjectMocks
    private GetUsersService getUsersService;

    // 헬퍼 메서드: 간단한 User 엔티티 생성 (필요한 최소 필드만 사용)
    private User createUser(long id) {
        return User.builder()
                .id(id)
                .email("user" + id + "@example.com")
                .address("Address " + id)
                .name("Name " + id)
                .phoneNumber("010-0000-000" + id)
                .role("ROLE_USER")
                .createdAt(java.time.LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("입력받은 id [1, 2, 3]에 대한 유저 정보가 정상적으로 리턴된다.")
    void testGetUsersByIds() {
        // given
        List<Long> userIds = List.of(1L, 2L, 3L);
        User user1 = createUser(1L);
        User user2 = createUser(2L);
        User user3 = createUser(3L);
        List<User> users = List.of(user1, user2, user3);

        List<UserInfoDto> expectedDtos = List.of(
                UserInfoDto.of(user1),
                UserInfoDto.of(user2),
                UserInfoDto.of(user3)
        );

        // when
        when(getUserPort.getUsersByIds(userIds)).thenReturn(users);
        List<UserInfoDto> result = getUsersService.getUsersByIds(userIds);

        // then
        assertEquals(expectedDtos, result);
    }

    @Test
    @DisplayName("입력받은 id [1, 2, 3]에 대한 유저 정보가 없으면 빈 리스트가 리턴된다.")
    void testGetUsersByIds_empty() {
        // given
        List<Long> userIds = List.of(1L, 2L, 3L);
        when(getUserPort.getUsersByIds(userIds)).thenReturn(List.of());

        // when
        List<UserInfoDto> result = getUsersService.getUsersByIds(userIds);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("이름으로 조회한 유저 정보가 정상적으로 리턴된다.")
    void testGetUsersByName() {
        // given
        String name = "John";
        User user1 = createUser(1L);
        User user2 = createUser(2L);
        List<User> users = List.of(user1, user2);
        List<UserInfoDto> expectedDtos = List.of(
                UserInfoDto.of(user1),
                UserInfoDto.of(user2)
        );

        // when
        when(getUserPort.getUsersByName(name)).thenReturn(users);
        List<UserInfoDto> result = getUsersService.getUsersByName(name);

        // then
        assertEquals(expectedDtos, result);
    }

    @Test
    @DisplayName("이름으로 조회했으나 유저 정보가 없으면 빈 리스트가 리턴된다.")
    void testGetUsersByName_empty() {
        // given
        String name = "NonExistent";
        when(getUserPort.getUsersByName(name)).thenReturn(List.of());

        // when
        List<UserInfoDto> result = getUsersService.getUsersByName(name);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("이메일로 조회한 유저 정보가 정상적으로 리턴된다.")
    void testGetUserByEmail_found() {
        // given
        String email = "user1@example.com";
        User user = createUser(1L);
        List<UserInfoDto> expectedDtos = List.of(UserInfoDto.of(user));

        // when
        when(getUserPort.getUsersByEmail(email)).thenReturn(List.of(user));
        List<UserInfoDto> result = getUsersService.getUsersByEmail(email);

        // then
        assertEquals(expectedDtos, result);
    }

    @Test
    @DisplayName("이메일로 조회했으나 유저 정보가 없으면 빈 리스트가 리턴된다.")
    void testGetUserByEmail_notFound() {
        // given
        String email = "notfound@example.com";
        User user = createUser(1L);
        List<UserInfoDto> expectedDtos = List.of(UserInfoDto.of(user));

        // when
        when(getUserPort.getUsersByEmail(email)).thenReturn(List.of(user));
        List<UserInfoDto> result = getUsersService.getUsersByEmail(email);

        // then
        assertEquals(expectedDtos, result);
    }

    @Test
    @DisplayName("역할로 조회한 유저 정보가 정상적으로 리턴된다.")
    void testGetUsersByRole() {
        // given
        String role = "ROLE_USER";
        User user1 = createUser(1L);
        User user2 = createUser(2L);
        List<User> users = List.of(user1, user2);
        List<UserInfoDto> expectedDtos = List.of(
                UserInfoDto.of(user1),
                UserInfoDto.of(user2)
        );

        // when
        when(getUserPort.getUsersByRole(role)).thenReturn(users);
        List<UserInfoDto> result = getUsersService.getUsersByRole(role);

        // then
        assertEquals(expectedDtos, result);
    }

    @Test
    @DisplayName("역할로 조회했으나 유저 정보가 없으면 빈 리스트가 리턴된다.")
    void testGetUsersByRole_empty() {
        // given
        String role = "ROLE_UNKNOWN";
        when(getUserPort.getUsersByRole(role)).thenReturn(List.of());

        // when
        List<UserInfoDto> result = getUsersService.getUsersByRole(role);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("전화번호로 조회한 유저 정보가 정상적으로 리턴된다.")
    void testGetUserByPhoneNumber() {
        // given
        String phoneNumber = "010-1234-5678";
        User user1 = createUser(1L);
        User user2 = createUser(2L);
        List<User> users = List.of(user1, user2);
        List<UserInfoDto> expectedDtos = List.of(
                UserInfoDto.of(user1),
                UserInfoDto.of(user2)
        );

        // when
        when(getUserPort.getUsersByPhoneNumber(phoneNumber)).thenReturn(users);
        List<UserInfoDto> result = getUsersService.getUserByPhoneNumber(phoneNumber);

        // then
        assertEquals(expectedDtos, result);
    }

    @Test
    @DisplayName("전화번호로 조회했으나 유저 정보가 없으면 빈 리스트가 리턴된다.")
    void testGetUserByPhoneNumber_empty() {
        // given
        String phoneNumber = "010-0000-0000";
        when(getUserPort.getUsersByPhoneNumber(phoneNumber)).thenReturn(List.of());

        // when
        List<UserInfoDto> result = getUsersService.getUserByPhoneNumber(phoneNumber);

        // then
        assertTrue(result.isEmpty());
    }
}
