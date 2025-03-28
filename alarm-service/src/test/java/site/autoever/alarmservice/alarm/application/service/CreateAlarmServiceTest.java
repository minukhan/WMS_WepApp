package site.autoever.alarmservice.alarm.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import site.autoever.alarmservice.alarm.adapter.in.dto.CreateAlarmRequest;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;
import site.autoever.alarmservice.alarm.application.port.out.CreateAlarmPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateAlarmService 단위 테스트")
class CreateAlarmServiceTest {

    @InjectMocks
    private CreateAlarmService createAlarmService;

    @Mock
    private CreateAlarmPort createAlarmPort;

    @Test
    @DisplayName("정상 요청 시 알람이 정상적으로 생성되어야 한다")
    void 정상요청_알람생성_성공() {
        // Given: CreateAlarmRequest에서 엔티티로 변환
        CreateAlarmRequest request = mock(CreateAlarmRequest.class);
        Alarm alarm = Alarm.builder()
                .userId(1L)
                .message("Test alarm")
                .isRead(false)
                .createdAt(Instant.now())
                .build();
        when(request.toEntity()).thenReturn(alarm);

        // And: 저장 시 id가 부여된 Alarm 반환 (CreateAlarmPort.save)
        Alarm savedAlarm = Alarm.builder()
                .id("alarm1")
                .userId(1L)
                .message("Test alarm")
                .isRead(false)
                .createdAt(alarm.getCreatedAt())
                .build();
        when(createAlarmPort.save(alarm)).thenReturn(Mono.just(savedAlarm));

        // When: 알람 생성 서비스 호출
        Mono<AlarmResponse> result = createAlarmService.createAlarm(request);

        // Then: 반환된 AlarmResponse가 저장된 Alarm 정보와 일치하는지 확인
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(savedAlarm.getId(), response.id(), "알람 ID가 일치해야 한다");
                    assertEquals(savedAlarm.getUserId(), response.userId(), "사용자 ID가 일치해야 한다");
                    assertEquals(savedAlarm.getMessage(), response.message(), "메시지가 일치해야 한다");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("알람 저장 실패 시 오류가 전달되어야 한다")
    void 알람저장_실패_오류전달() {
        // Given: CreateAlarmRequest에서 엔티티로 변환
        CreateAlarmRequest request = mock(CreateAlarmRequest.class);
        Alarm alarm = Alarm.builder()
                .userId(1L)
                .message("Test alarm")
                .isRead(false)
                .createdAt(Instant.now())
                .build();
        when(request.toEntity()).thenReturn(alarm);

        // And: CreateAlarmPort.save 호출 시 예외 발생
        RuntimeException exception = new RuntimeException("저장 실패");
        when(createAlarmPort.save(alarm)).thenReturn(Mono.error(exception));

        // When: 알람 생성 서비스 호출
        Mono<AlarmResponse> result = createAlarmService.createAlarm(request);

        // Then: Mono에서 동일한 예외가 발생하는지 확인
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("저장 실패"))
                .verify();
    }
}
