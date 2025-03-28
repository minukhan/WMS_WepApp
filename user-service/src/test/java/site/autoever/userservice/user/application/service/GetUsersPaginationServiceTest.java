package site.autoever.userservice.user.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import site.autoever.userservice.user.application.dto.UserInfoDto;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.port.out.GetUsersPaginationPort;

@ExtendWith(MockitoExtension.class)
class GetUsersPaginationServiceTest {

    @Mock
    private GetUsersPaginationPort getUsersPaginationPort;

    @InjectMocks
    private GetUsersPaginationService getUsersService;

    // 헬퍼 메서드: 간단한 User 엔티티 생성
    private User createUser(long id, String name, String role, String phoneNumber) {
        return User.builder()
                .id(id)
                .email("user" + id + "@example.com")
                .address("Address " + id)
                .name(name)
                .phoneNumber(phoneNumber)
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("전체 사용자 조회 시 UserInfoDto로 정상 매핑된다")
    void testGetAllUsers() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(
                createUser(1L, "Alice", "ROLE_USER", "010-1111-1111"),
                createUser(2L, "Bob", "ROLE_ADMIN", "010-2222-2222")
        );
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());
        List<UserInfoDto> expectedDtos = users.stream().map(UserInfoDto::of).toList();

        // when
        when(getUsersPaginationPort.getAllUsers(pageable)).thenReturn(userPage);
        Page<UserInfoDto> result = getUsersService.getAllUsers(pageable);

        // then
        assertEquals(expectedDtos, result.getContent());
        assertEquals(userPage.getTotalElements(), result.getTotalElements());
    }

    @Test
    @DisplayName("이름으로 사용자 조회 시 UserInfoDto로 정상 매핑된다")
    void testGetUsersByName() {
        // given
        String name = "Alice";
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(createUser(1L, "Alice", "ROLE_USER", "010-1111-1111"));
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());
        List<UserInfoDto> expectedDtos = users.stream().map(UserInfoDto::of).toList();

        // when
        when(getUsersPaginationPort.getUsersByName(name, pageable)).thenReturn(userPage);
        Page<UserInfoDto> result = getUsersService.getUsersByName(name, pageable);

        // then
        assertEquals(expectedDtos, result.getContent());
        assertEquals(userPage.getTotalElements(), result.getTotalElements());
    }

    @Test
    @DisplayName("역할로 사용자 조회 시 UserInfoDto로 정상 매핑된다")
    void testGetUsersByRole() {
        // given
        String role = "ROLE_ADMIN";
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(createUser(2L, "Bob", role, "010-2222-2222"));
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());
        List<UserInfoDto> expectedDtos = users.stream().map(UserInfoDto::of).toList();

        // when
        when(getUsersPaginationPort.getUsersByRole(role, pageable)).thenReturn(userPage);
        Page<UserInfoDto> result = getUsersService.getUsersByRole(role, pageable);

        // then
        assertEquals(expectedDtos, result.getContent());
        assertEquals(userPage.getTotalElements(), result.getTotalElements());
    }

    @Test
    @DisplayName("전화번호로 사용자 조회 시 UserInfoDto로 정상 매핑된다")
    void testGetUserByPhoneNumber() {
        // given
        String phoneNumber = "010-1111-1111";
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(createUser(1L, "Alice", "ROLE_USER", phoneNumber));
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());
        List<UserInfoDto> expectedDtos = users.stream().map(UserInfoDto::of).toList();

        // when
        when(getUsersPaginationPort.getUsersByPhoneNumber(phoneNumber, pageable)).thenReturn(userPage);
        Page<UserInfoDto> result = getUsersService.getUserByPhoneNumber(phoneNumber, pageable);

        // then
        assertEquals(expectedDtos, result.getContent());
        assertEquals(userPage.getTotalElements(), result.getTotalElements());
    }

    @Test
    @DisplayName("이메일로 사용자 조회 시 UserInfoDto로 정상 매핑된다")
    void testGetUserByEmail() {
        // given
        String email = "user1@example.com";
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = List.of(createUser(1L, "Alice", "ROLE_USER", "010-1111-1111"));
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());
        List<UserInfoDto> expectedDtos = users.stream().map(UserInfoDto::of).toList();

        // when
        when(getUsersPaginationPort.getUsersByEmail(email, pageable)).thenReturn(userPage);
        Page<UserInfoDto> result = getUsersService.getUserByEmail(email, pageable);

        // then
        assertEquals(expectedDtos, result.getContent());
        assertEquals(userPage.getTotalElements(), result.getTotalElements());
    }


}
