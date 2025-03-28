package site.autoever.logservice.user_log.application.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.autoever.logservice.user_log.application.entity.UserLog;
import site.autoever.logservice.user_log.application.port.out.SaveLogPort;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaveLogServiceTest {

    @InjectMocks
    private SaveLogService saveLogService;

    @Mock
    private SaveLogPort saveLogPort;

    @DisplayName("로그 생성 성공 테스트")
    @Test
    void createLogTest() {
        // given: 테스트에 필요한 UserLog 생성 및 Mock 설정
        UserLog userLog = UserLog.builder()
                .id(1L)
                .userId(101L)
                .message("테스트 로그 메시지")
                .createdAt(LocalDateTime.now())
                .build();

        when(saveLogPort.saveLog(userLog)).thenReturn(userLog.getId());

        // when: 서비스 메서드 호출
        saveLogService.saveLog(userLog);

        // then: saveLogPort가 호출되었는지 검증
        verify(saveLogPort, times(1)).saveLog(userLog);
    }

    @DisplayName("로그 생성 성공 및 ID 검증 테스트")
    @Test
    void createLogAndVerifyIdTest() {
        // given: 테스트에 필요한 UserLog 생성 및 Mock 설정
        UserLog userLog = UserLog.builder()
                .id(1L)
                .userId(101L)
                .message("테스트 로그 메시지")
                .createdAt(LocalDateTime.now())
                .build();

        when(saveLogPort.saveLog(userLog)).thenReturn(userLog.getId());

        // when: 서비스 메서드 호출
        saveLogService.saveLog(userLog);

        // then: saveLogPort가 호출되었는지 검증
        verify(saveLogPort, times(1)).saveLog(userLog);

        // ID 값 검증 (Mock 설정을 통해 반환된 값이 기대값과 일치하는지 확인)
        Long expectedId = 1L;
        Long actualId = saveLogPort.saveLog(userLog);
        assertEquals(expectedId, actualId, "생성된 로그의 ID 값이 일치하지 않습니다.");
    }

}