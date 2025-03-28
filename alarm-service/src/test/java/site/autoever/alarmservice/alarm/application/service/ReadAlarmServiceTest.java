package site.autoever.alarmservice.alarm.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.Instant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import site.autoever.alarmservice.alarm.adapter.in.dto.AlarmResponse;
import site.autoever.alarmservice.alarm.adapter.in.dto.ReadAlarmRequest;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;
import site.autoever.alarmservice.alarm.application.port.out.GetAlarmPort;
import site.autoever.alarmservice.alarm.application.port.out.UpdateAlarmPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReadAlarmService 단위 테스트")
class ReadAlarmServiceTest {

    @InjectMocks
    private ReadAlarmService readAlarmService;

    @Mock
    private GetAlarmPort getAlarmPort;

    @Mock
    private UpdateAlarmPort updateAlarmPort;

    private final String ALARM_ID = "alarm123";
    private final long USER_ID = 1L;

    @Test
    @DisplayName("알람이 이미 읽음 상태이면 업데이트 없이 그대로 반환해야 한다")
    void readAlarm_이미읽음() {
        // Given: 이미 읽음 상태인 알람
        ReadAlarmRequest request = mock(ReadAlarmRequest.class);
        when(request.alarmId()).thenReturn(ALARM_ID);

        Alarm alreadyReadAlarm = Alarm.builder()
                .id(ALARM_ID)
                .userId(USER_ID)
                .message("기존 알람")
                .isRead(true)
                .createdAt(Instant.now())
                .build();

        when(getAlarmPort.findAlarmById(ALARM_ID)).thenReturn(Mono.just(alreadyReadAlarm));

        // When: 알람 읽기 서비스 호출
        Mono<AlarmResponse> result = readAlarmService.readAlarm(request);

        // Then: 업데이트 없이 기존 알람이 반환되어야 함
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(ALARM_ID, response.id());
                    assertEquals(USER_ID, response.userId());
                    assertEquals("기존 알람", response.message());
                    assertEquals(true, response.isRead());
                })
                .verifyComplete();

        // 업데이트 포트가 호출되지 않았는지 확인
        verify(updateAlarmPort, never()).updateAlarm(any());
    }

    @Test
    @DisplayName("읽지 않은 알람이면 읽음 처리 후 업데이트 되어야 한다")
    void readAlarm_읽지않음() {
        // Given: 읽지 않은 알람
        ReadAlarmRequest request = mock(ReadAlarmRequest.class);
        when(request.alarmId()).thenReturn(ALARM_ID);

        Alarm unreadAlarm = Alarm.builder()
                .id(ALARM_ID)
                .userId(USER_ID)
                .message("새로운 알람")
                .isRead(false)
                .createdAt(Instant.now())
                .build();

        // 업데이트 후 읽음 처리된 알람
        Alarm updatedAlarm = Alarm.builder()
                .id(ALARM_ID)
                .userId(USER_ID)
                .message("새로운 알람")
                .isRead(true)
                .createdAt(unreadAlarm.getCreatedAt())
                .build();

        when(getAlarmPort.findAlarmById(ALARM_ID)).thenReturn(Mono.just(unreadAlarm));
        when(updateAlarmPort.updateAlarm(any(Alarm.class))).thenReturn(Mono.just(updatedAlarm));

        // When: 알람 읽기 서비스 호출
        Mono<AlarmResponse> result = readAlarmService.readAlarm(request);

        // Then: 알람이 업데이트되고 반환되어야 함
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(ALARM_ID, response.id());
                    assertEquals(USER_ID, response.userId());
                    assertEquals("새로운 알람", response.message());
                    assertEquals(true, response.isRead());
                })
                .verifyComplete();

        // 업데이트 포트가 정확히 한 번 호출되었는지 확인
        verify(updateAlarmPort, times(1)).updateAlarm(any(Alarm.class));
    }

    @Test
    @DisplayName("존재하지 않는 알람 ID 조회 시 빈 결과를 반환해야 한다")
    void readAlarm_알람없음() {
        // Given: 존재하지 않는 알람 ID
        ReadAlarmRequest request = mock(ReadAlarmRequest.class);
        when(request.alarmId()).thenReturn(ALARM_ID);

        when(getAlarmPort.findAlarmById(ALARM_ID)).thenReturn(Mono.empty());

        // When: 알람 읽기 서비스 호출
        Mono<AlarmResponse> result = readAlarmService.readAlarm(request);

        // Then: 빈 결과 반환 검증
        StepVerifier.create(result)
                .verifyComplete();
    }
}
