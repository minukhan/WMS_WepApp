package site.autoever.logservice.user_log.application.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.autoever.logservice.infrastructure.util.AsyncLogProcessor;
import site.autoever.logservice.user_log.application.entity.UserLog;

@ExtendWith(MockitoExtension.class)
class AsyncSaveLogServiceTest {

    @Mock
    private AsyncLogProcessor logProcessor;

    @InjectMocks
    private AsyncSaveLogService asyncSaveLogService;

    @Test
    @DisplayName("비동기 로그 저장 호출 테스트: 로그가 enqueue 된다")
    void saveLog_shouldEnqueueLog() {
        // given: 테스트에 사용할 UserLog 생성
        UserLog userLog = UserLog.builder()
                .id(1L)
                .userId(101L)
                .message("비동기 테스트 로그 메시지")
                .createdAt(LocalDateTime.now())
                .build();

        // when: saveLog() 호출
        asyncSaveLogService.saveLog(userLog);

        // then: logProcessor.enqueueLog(userLog)가 한 번 호출되었는지 검증
        verify(logProcessor, times(1)).enqueueLog(userLog);
    }
}
