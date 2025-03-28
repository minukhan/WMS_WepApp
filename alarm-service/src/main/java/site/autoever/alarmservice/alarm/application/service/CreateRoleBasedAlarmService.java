package site.autoever.alarmservice.alarm.application.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import site.autoever.alarmservice.alarm.adapter.in.dto.AlarmResponse;
import site.autoever.alarmservice.alarm.adapter.in.dto.CreateRoleBasedAlarmRequest;
import site.autoever.alarmservice.alarm.application.domain.model.Alarm;
import site.autoever.alarmservice.alarm.application.domain.model.Role;
import site.autoever.alarmservice.alarm.application.dto.UserInfoDto;
import site.autoever.alarmservice.alarm.application.port.in.CreateRoleBasedAlarmUseCase;
import site.autoever.alarmservice.alarm.application.port.out.CreateAlarmPort;
import site.autoever.alarmservice.alarm.application.port.out.GetUsersPort;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateRoleBasedAlarmService implements CreateRoleBasedAlarmUseCase {

    private final GetUsersPort getUsersPort;
    private final CreateAlarmPort createAlarmPort;

    @Override
    public Flux<AlarmResponse> createRoleBasedAlarms(CreateRoleBasedAlarmRequest request) {
        log.info("Alarm creation started for role {} with message: {}", request.role(),
                request.message());

        // 만약 입력이 ROLE_ADMIN이면 ROLE_ADMIN만 조회
        if (Role.ROLE_ADMIN.name().equals(request.role())) {
            return fetchUsersByRole(request.role())
                    .flatMapMany(Flux::fromIterable)
                    .map(UserInfoDto::userId)
                    .flatMap(userId -> createAlarm(userId, request.message(), request.type()));
        }

        // ROLE_INFRA_MANAGER 또는 ROLE_WAREHOUSE_MANAGER인 경우, 해당 역할과 함께 ROLE_ADMIN도 조회
        Mono<List<UserInfoDto>> usersForInputRoleMono = fetchUsersByRole(request.role());
        Mono<List<UserInfoDto>> usersForAdminMono = fetchUsersByRole(Role.ROLE_ADMIN.name());

        return Flux.merge(usersForInputRoleMono, usersForAdminMono)
                .flatMapIterable(list -> list)  // List<UserInfoDto> 를 Flux<UserInfoDto> 로 변환
                .collect(Collectors.toSet())     // 중복 제거: Mono<Set<UserInfoDto>>
                .flatMapMany(Flux::fromIterable)  // Mono<Set<UserInfoDto>> -> Flux<UserInfoDto>
                .map(UserInfoDto::userId)
                .flatMap(userId -> createAlarm(userId, request.message(), request.type()));
    }

    // Feign 동기 호출을 논블로킹 스레드로 실행하여 Mono<List<UserInfoDto>>로 감싸기
    private Mono<List<UserInfoDto>> fetchUsersByRole(String role) {
        return Mono.fromCallable(() -> {
            List<UserInfoDto> users = getUsersPort.getUsersByRole(role);
            log.info("Fetched {} users for role {}", users.size(), role);
            return users;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    // 각 사용자에 대해 알람 생성
    private Mono<AlarmResponse> createAlarm(Long userId, String message, String type) {
        log.info("Creating alarm for userId {} with message: {}", userId, message);
        Alarm alarm = Alarm.builder()
                .userId(userId)
                .message(message)
                .type(type)
                .isRead(false)
                .createdAt(Instant.now())
                .build();
        return createAlarmPort.save(alarm)
                .doOnSuccess(savedAlarm ->
                        log.info("Alarm created with id {} for userId {}", savedAlarm.getId(),
                                userId))
                .map(AlarmResponse::fromEntity);
    }
}
