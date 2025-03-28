package site.autoever.alarmservice.alarm.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import site.autoever.alarmservice.alarm.adapter.in.dto.AlarmResponse;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;
import site.autoever.alarmservice.alarm.application.port.out.GetAlarmsPort;
import site.autoever.alarmservice.infrastructure.util.UserIdResolver;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetAlarmsService 단위 테스트")
class GetAlarmsServiceTest {

    @InjectMocks
    private GetAlarmsService getAlarmsService;

    @Mock
    private GetAlarmsPort getAlarmsPort;

    @Mock
    private UserIdResolver userIdResolver;

    private final long TEST_USER_ID = 1L;


    @Test
    @DisplayName("모든 알람 조회 시, 해당 사용자 ID의 모든 알람을 반환해야 한다")
    void getAllUserAlarms_성공() {
        // Given: 현재 로그인된 사용자 ID 반환
        when(userIdResolver.getCurrentUserId()).thenReturn(Mono.just(TEST_USER_ID));

        // And: 해당 사용자의 알람 2개 모킹
        Alarm alarm1 = Alarm.builder()
                .id("1")
                .userId(TEST_USER_ID)
                .message("첫 번째 알람")
                .isRead(true)
                .createdAt(Instant.now())
                .build();

        Alarm alarm2 = Alarm.builder()
                .id("2")
                .userId(TEST_USER_ID)
                .message("두 번째 알람")
                .isRead(false)
                .createdAt(Instant.now())
                .build();

        when(getAlarmsPort.findAllAlarmsByUserId(TEST_USER_ID))
                .thenReturn(Flux.fromIterable(List.of(alarm1, alarm2)));

        // When: 서비스 호출
        Flux<AlarmResponse> result = getAlarmsService.getAllUserAlarms();

        // Then: 결과 검증
        StepVerifier.create(result.collectList())
                .assertNext(alarmList -> {
                    assertEquals(2, alarmList.size(), "총 2개의 알람이 반환되어야 한다");
                    assertEquals("1", alarmList.get(0).id());
                    assertEquals("첫 번째 알람", alarmList.get(0).message());
                    assertEquals("2", alarmList.get(1).id());
                    assertEquals("두 번째 알람", alarmList.get(1).message());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("읽지 않은 알람 조회 시, 읽지 않은 알람만 반환해야 한다")
    void getUnreadUserAlarms_성공() {
        // Given: 현재 로그인된 사용자 ID 반환
        when(userIdResolver.getCurrentUserId()).thenReturn(Mono.just(TEST_USER_ID));

        // And: 해당 사용자의 읽지 않은 알람 1개 모킹
        Alarm unreadAlarm = Alarm.builder()
                .id("3")
                .userId(TEST_USER_ID)
                .message("읽지 않은 알람")
                .isRead(false)
                .createdAt(Instant.now())
                .build();

        when(getAlarmsPort.findUnreadAlarmsByUserId(TEST_USER_ID))
                .thenReturn(Flux.just(unreadAlarm));

        // When: 서비스 호출
        Flux<AlarmResponse> result = getAlarmsService.getUnreadUserAlarms();

        // Then: 결과 검증
        StepVerifier.create(result)
                .assertNext(alarm -> {
                    assertEquals("3", alarm.id());
                    assertEquals("읽지 않은 알람", alarm.message());
                    assertEquals(TEST_USER_ID, alarm.userId());
                    assertEquals(false, alarm.isRead());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("사용자가 로그인하지 않은 경우, 모든 알람 조회 시 빈 결과를 반환해야 한다")
    void getAllUserAlarms_사용자없음() {
        // Given: 로그인되지 않은 경우 (Mono.empty())
        when(userIdResolver.getCurrentUserId()).thenReturn(Mono.empty());

        // When: 서비스 호출
        Flux<AlarmResponse> result = getAlarmsService.getAllUserAlarms();

        // Then: 빈 결과가 반환되어야 함
        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    @DisplayName("사용자가 로그인하지 않은 경우, 읽지 않은 알람 조회 시 빈 결과를 반환해야 한다")
    void getUnreadUserAlarms_사용자없음() {
        // Given: 로그인되지 않은 경우 (Mono.empty())
        when(userIdResolver.getCurrentUserId()).thenReturn(Mono.empty());

        // When: 서비스 호출
        Flux<AlarmResponse> result = getAlarmsService.getUnreadUserAlarms();

        // Then: 빈 결과가 반환되어야 함
        StepVerifier.create(result)
                .verifyComplete();
    }
}
