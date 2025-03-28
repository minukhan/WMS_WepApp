package site.autoever.logservice.user_log.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import site.autoever.logservice.user_log.application.dto.UserIdsDto;
import site.autoever.logservice.user_log.application.dto.UserInfoDto;
import site.autoever.logservice.user_log.application.dto.UserLogInfoDto;
import site.autoever.logservice.user_log.application.entity.UserLog;
import site.autoever.logservice.user_log.application.port.out.GetLogsPort;
import site.autoever.logservice.user_log.application.port.out.GetUsersPort;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllLogsServiceTest {

    @InjectMocks
    private GetAllLogsService getAllLogsService;

    @Mock
    private GetUsersPort getUsersPort;

    @Mock
    private GetLogsPort getLogsPort;

    @Test
    @DisplayName("모든 로그 조회 테스트")
    void getAllLogs() {
        // given: 로그와 유저 정보 준비
        Pageable pageable = PageRequest.of(0, 10);
        List<UserLog> logs = List.of(
                UserLog.builder().id(1L).userId(101L).message("테스트 로그").createdAt(LocalDateTime.now()).build()
        );
        Page<UserLog> userLogPage = new PageImpl<>(logs);
        UserInfoDto userInfoDto = new UserInfoDto(101L, "서울시 강남구", "test@example.com", "테스트 유저", "010-1234-5678", "ADMIN", LocalDateTime.now());

        when(getLogsPort.getLogs(pageable)).thenReturn(userLogPage);
        when(getUsersPort.getUsersByIds(any(UserIdsDto.class))).thenReturn(List.of(userInfoDto));

        // when: 서비스 호출
        Page<UserLogInfoDto> result = getAllLogsService.getAllLogs(pageable);

        // then: 결과 검증
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("테스트 로그", result.getContent().get(0).message());
        assertEquals("test@example.com", result.getContent().get(0).email());
    }

    @Test
    @DisplayName("메시지로 로그 조회 테스트")
    void getAllLogsByMessage() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String message = "테스트 로그";
        List<UserLog> logs = List.of(
                UserLog.builder().id(1L).userId(101L).message(message).createdAt(LocalDateTime.now()).build()
        );
        Page<UserLog> userLogPage = new PageImpl<>(logs);
        UserInfoDto userInfoDto = new UserInfoDto(101L, "서울시 강남구", "test@example.com", "테스트 유저", "010-1234-5678", "ADMIN", LocalDateTime.now());

        when(getLogsPort.getLogsByMessage(message, pageable)).thenReturn(userLogPage);
        when(getUsersPort.getUsersByIds(any(UserIdsDto.class))).thenReturn(List.of(userInfoDto));

        // when
        Page<UserLogInfoDto> result = getAllLogsService.getAllLogsByMessage(message, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(message, result.getContent().get(0).message());
    }

    @Test
    @DisplayName("날짜로 로그 조회 테스트")
    void getAllLogsByDate() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        LocalDateTime date = LocalDateTime.now();
        List<UserLog> logs = List.of(
                UserLog.builder().id(1L).userId(101L).message("날짜별 로그").createdAt(date).build()
        );
        Page<UserLog> userLogPage = new PageImpl<>(logs);
        UserInfoDto userInfoDto = new UserInfoDto(101L, "서울시 강남구", "test@example.com", "테스트 유저", "010-1234-5678", "ADMIN", date);

        when(getLogsPort.getLogsByDate(date.toLocalDate(), pageable)).thenReturn(userLogPage);
        when(getUsersPort.getUsersByIds(any(UserIdsDto.class))).thenReturn(List.of(userInfoDto));

        // when
        Page<UserLogInfoDto> result = getAllLogsService.getAllLogsByDate(date.toLocalDate(), pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("날짜별 로그", result.getContent().get(0).message());
    }

    @Test
    @DisplayName("이름으로 로그 조회 테스트")
    void getAllLogsByName() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String name = "테스트 유저";
        List<UserInfoDto> users = List.of(new UserInfoDto(101L, "서울시 강남구", "test@example.com", name, "010-1234-5678", "ADMIN", LocalDateTime.now()));
        List<UserLog> logs = List.of(
                UserLog.builder().id(1L).userId(101L).message("이름별 로그").createdAt(LocalDateTime.now()).build()
        );
        Page<UserLog> userLogPage = new PageImpl<>(logs);

        when(getUsersPort.getUsersByName(name)).thenReturn(users);
        when(getLogsPort.getLogsByUserIds(anyList(), eq(pageable))).thenReturn(userLogPage);

        // when
        Page<UserLogInfoDto> result = getAllLogsService.getAllLogsByName(name, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("이름별 로그", result.getContent().get(0).message());
    }

    @Test
    @DisplayName("역할로 로그 조회 테스트")
    void getAllLogsByRole() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String role = "ADMIN";
        List<UserInfoDto> users = List.of(new UserInfoDto(101L, "서울시 강남구", "test@example.com", "테스트 유저", "010-1234-5678", role, LocalDateTime.now()));
        List<UserLog> logs = List.of(
                UserLog.builder().id(1L).userId(101L).message("역할별 로그").createdAt(LocalDateTime.now()).build()
        );
        Page<UserLog> userLogPage = new PageImpl<>(logs);

        when(getUsersPort.getUsersByRole(role)).thenReturn(users);
        when(getLogsPort.getLogsByUserIds(anyList(), eq(pageable))).thenReturn(userLogPage);

        // when
        Page<UserLogInfoDto> result = getAllLogsService.getAllLogsByRole(role, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("역할별 로그", result.getContent().get(0).message());
    }

    @Test
    @DisplayName("이메일로 로그 조회 테스트")
    void getAllLogsByEmail() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        String email = "test@example.com";
        List<UserInfoDto> users = List.of(new UserInfoDto(101L, "서울시 강남구", email, "테스트 유저", "010-1234-5678", "ADMIN", LocalDateTime.now()));
        List<UserLog> logs = List.of(
                UserLog.builder().id(1L).userId(101L).message("이메일별 로그").createdAt(LocalDateTime.now()).build()
        );
        Page<UserLog> userLogPage = new PageImpl<>(logs);

        when(getUsersPort.getUsersByEmail(email)).thenReturn(users);
        when(getLogsPort.getLogsByUserIds(anyList(), eq(pageable))).thenReturn(userLogPage);

        // when
        Page<UserLogInfoDto> result = getAllLogsService.getAllLogsByEmail(email, pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("이메일별 로그", result.getContent().get(0).message());
    }

    @Test
    @DisplayName("전화번호로 로그 조회 테스트")
    void getAllLogsByPhoneNumber() {
        // given: 유저 정보와 로그 데이터 준비
        Pageable pageable = PageRequest.of(0, 10);
        String phoneNumber = "010-1234-5678";

        List<UserInfoDto> users = List.of(
                new UserInfoDto(101L, "서울시 강남구", "test@example.com", "테스트 유저", phoneNumber, "USER", LocalDateTime.now())
        );

        List<UserLog> logs = List.of(
                UserLog.builder().id(1L).userId(101L).message("전화번호별 로그").createdAt(LocalDateTime.now()).build()
        );

        Page<UserLog> userLogPage = new PageImpl<>(logs);

        when(getUsersPort.getUsersByPhoneNumber(phoneNumber)).thenReturn(users);
        when(getLogsPort.getLogsByUserIds(anyList(), eq(pageable))).thenReturn(userLogPage);

        // when: 서비스 호출
        Page<UserLogInfoDto> result = getAllLogsService.getAllLogsByPhoneNumber(phoneNumber, pageable);

        // then: 결과 검증
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("전화번호별 로그", result.getContent().get(0).message());
        assertEquals(phoneNumber, result.getContent().get(0).phoneNumber());
    }


}
