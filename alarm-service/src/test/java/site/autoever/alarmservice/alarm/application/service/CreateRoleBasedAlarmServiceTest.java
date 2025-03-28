package site.autoever.alarmservice.alarm.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
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
import site.autoever.alarmservice.alarm.adapter.in.dto.CreateRoleBasedAlarmRequest;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;
import site.autoever.alarmservice.alarm.application.domain.model.Role;
import site.autoever.alarmservice.alarm.application.dto.UserInfoDto;
import site.autoever.alarmservice.alarm.application.port.out.CreateAlarmPort;
import site.autoever.alarmservice.alarm.application.port.out.GetUsersPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateRoleBasedAlarmService 단위 테스트")
public class CreateRoleBasedAlarmServiceTest {

    @Mock
    private GetUsersPort getUsersPort;

    @Mock
    private CreateAlarmPort createAlarmPort;

    @InjectMocks
    private CreateRoleBasedAlarmService createRoleBasedAlarmService;

    @Test
    @DisplayName("ROLE_ADMIN 입력 시 admin 사용자만 조회하여 알람 생성")
    void testCreateRoleBasedAlarmForAdmin() {
        // Given: ROLE_ADMIN 입력
        CreateRoleBasedAlarmRequest request = mock(CreateRoleBasedAlarmRequest.class);
        when(request.role()).thenReturn(Role.ROLE_ADMIN.name());
        when(request.message()).thenReturn("Admin alarm message");

        // And: admin 사용자 1명 (userId: 10L)
        // UserInfoDto 생성 시 모든 필드를 채운다.
        UserInfoDto adminUser = new UserInfoDto(
                10L,
                "서울시 강남구",
                "admin@example.com",
                "관리자",
                "010-1234-5678",
                Role.ROLE_ADMIN.name(),
                LocalDateTime.now()
        );
        when(getUsersPort.getUsersByRole(Role.ROLE_ADMIN.name())).thenReturn(List.of(adminUser));

        // And: 생성된 알람 객체 반환 (Alarm ID는 문자열)
        Alarm alarm = Alarm.builder()
                .id("alarm1")
                .userId(10L)
                .message("Admin alarm message")
                .isRead(false)
                .createdAt(Instant.now())
                .build();
        when(createAlarmPort.save(any(Alarm.class))).thenReturn(Mono.just(alarm));

        // When: 알람 생성
        Flux<AlarmResponse> result = createRoleBasedAlarmService.createRoleBasedAlarms(request);

        // Then: 알람 응답 검증
        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(alarm.getId(), response.id(), "알람 ID가 일치해야 한다");
                    assertEquals(alarm.getUserId(), response.userId(), "사용자 ID가 일치해야 한다");
                    assertEquals(alarm.getMessage(), response.message(), "메시지가 일치해야 한다");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("ROLE_INFRA_MANAGER 입력 시 해당 역할 사용자와 admin 사용자 모두 조회하여 중복 제거 후 알람 생성")
    void testCreateRoleBasedAlarmForNonAdmin() {
        // Given: ROLE_INFRA_MANAGER 입력
        CreateRoleBasedAlarmRequest request = mock(CreateRoleBasedAlarmRequest.class);
        when(request.role()).thenReturn("ROLE_INFRA_MANAGER");
        when(request.message()).thenReturn("Infra manager alarm");

        // And: ROLE_INFRA_MANAGER 사용자 1명 (userId: 20L)
        UserInfoDto infraUser = new UserInfoDto(
                20L, "서울시 종로구", "infra@example.com", "인프라담당", "010-1111-2222", "ROLE_INFRA_MANAGER", LocalDateTime.now()
        );
        when(getUsersPort.getUsersByRole("ROLE_INFRA_MANAGER")).thenReturn(List.of(infraUser));

        // And: ROLE_ADMIN 사용자 1명 (userId: 10L)
        UserInfoDto adminUser = new UserInfoDto(
                10L, "서울시 강남구", "admin@example.com", "관리자", "010-3333-4444", "ROLE_ADMIN", LocalDateTime.now()
        );
        when(getUsersPort.getUsersByRole("ROLE_ADMIN")).thenReturn(List.of(adminUser));

        // 중복 제거된 사용자 목록 생성
        Set<Long> userIds = Set.of(infraUser.userId(), adminUser.userId());

        // 알람 저장 시, 각 사용자에 대해 Mock 설정
        when(createAlarmPort.save(any(Alarm.class)))
                .thenAnswer(invocation -> {
                    Alarm savedAlarm = invocation.getArgument(0);
                    return Mono.just(
                            Alarm.builder()
                                    .id(String.valueOf(savedAlarm.getUserId())) // ID를 userId로 설정 (테스트 용도)
                                    .userId(savedAlarm.getUserId())
                                    .message(savedAlarm.getMessage())
                                    .isRead(false)
                                    .createdAt(Instant.now())
                                    .build()
                    );
                });

        // When: 알람 생성
        Flux<AlarmResponse> result = createRoleBasedAlarmService.createRoleBasedAlarms(request);

        // Then: 두 사용자 모두에 대해 알람이 생성되는지 검증
        StepVerifier.create(result.collectList())
                .assertNext(list -> {
                    assertEquals(userIds.size(), list.size(), "중복 제거 후 생성된 알람 수가 일치해야 한다");

                    Set<Long> responseUserIds = list.stream()
                            .map(AlarmResponse::userId)
                            .collect(Collectors.toSet());

                    assertTrue(responseUserIds.contains(20L), "ROLE_INFRA_MANAGER 알람이 생성되어야 한다");
                    assertTrue(responseUserIds.contains(10L), "ROLE_ADMIN 알람이 생성되어야 한다");
                })
                .verifyComplete();
    }
}
