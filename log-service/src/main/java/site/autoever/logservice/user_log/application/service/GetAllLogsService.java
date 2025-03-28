package site.autoever.logservice.user_log.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.autoever.logservice.user_log.application.dto.UserIdsDto;
import site.autoever.logservice.user_log.application.dto.UserInfoDto;
import site.autoever.logservice.user_log.application.dto.UserLogInfoDto;
import site.autoever.logservice.user_log.application.entity.UserLog;
import site.autoever.logservice.user_log.application.port.in.GetAllLogsUseCase;
import site.autoever.logservice.user_log.application.port.out.GetLogsPort;
import site.autoever.logservice.user_log.application.port.out.GetUsersPort;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllLogsService implements GetAllLogsUseCase {

    private final GetUsersPort getUsersPort;
    private final GetLogsPort getLogsPort;

    @Override
    public Page<UserLogInfoDto> getAllLogs(Pageable pageable) {
        Page<UserLog> userLogs = getLogsPort.getLogs(pageable);
        return mapToUserLogInfoDto(userLogs);
    }

    @Override
    public Page<UserLogInfoDto> getAllLogsByMessage(String message, Pageable pageable) {
        Page<UserLog> userLogs = getLogsPort.getLogsByMessage(message, pageable);
        return mapToUserLogInfoDto(userLogs);
    }

    @Override
    public Page<UserLogInfoDto> getAllLogsByDate(LocalDate date, Pageable pageable) {
        Page<UserLog> userLogs = getLogsPort.getLogsByDate(date, pageable);
        log.info("size : {}", userLogs.getContent().size());
        return mapToUserLogInfoDto(userLogs);
    }

    @Override
    public Page<UserLogInfoDto> getAllLogsByName(String name, Pageable pageable) {
        List<UserInfoDto> users = getUsersPort.getUsersByName(name);
        return getLogsAndMap(users, pageable);
    }

    @Override
    public Page<UserLogInfoDto> getAllLogsByRole(String role, Pageable pageable) {
        List<UserInfoDto> users = getUsersPort.getUsersByRole(role);
        return getLogsAndMap(users, pageable);
    }

    @Override
    public Page<UserLogInfoDto> getAllLogsByEmail(String email, Pageable pageable) {
        log.info("input email: {}", email);
        List<UserInfoDto> users = getUsersPort.getUsersByEmail(email);
        return getLogsAndMap(users, pageable);
    }

    @Override
    public Page<UserLogInfoDto> getAllLogsByPhoneNumber(String phoneNumber, Pageable pageable) {
        List<UserInfoDto> users = getUsersPort.getUsersByPhoneNumber(phoneNumber);
        return getLogsAndMap(users, pageable);
    }

    /**
     * 사용자 정보 리스트와 Pageable을 받아서 로그를 조회하고 DTO로 변환
     */
    private Page<UserLogInfoDto> getLogsAndMap(List<UserInfoDto> users, Pageable pageable) {
        List<Long> userIds = extractUserIds(users);
        Page<UserLog> userLogs = getLogsPort.getLogsByUserIds(userIds, pageable);
        return mapToUserLogInfoDto(userLogs, users);
    }

    /**
     * UserLog 페이지와 UserInfoDto 리스트를 매핑하여 UserLogInfoDto로 변환
     */
    private Page<UserLogInfoDto> mapToUserLogInfoDto(Page<UserLog> userLogs, List<UserInfoDto> users) {
        Map<Long, UserInfoDto> userMap = users.stream()
                .collect(Collectors.toMap(UserInfoDto::userId, Function.identity()));
        return userLogs.map(userLog -> UserLogInfoDto.from(userLog, userMap.get(userLog.getUserId())));
    }

    /**
     * UserLog 페이지를 받아서 사용자 정보 조회 후 매핑하여 DTO로 변환
     */
    private Page<UserLogInfoDto> mapToUserLogInfoDto(Page<UserLog> userLogs) {
        List<Long> userIds = extractUserIds(userLogs);
        List<UserInfoDto> users = getUsersPort.getUsersByIds(UserIdsDto.of(userIds));
        return mapToUserLogInfoDto(userLogs, users);
    }

    /**
     * UserLog에서 userId 리스트 추출
     */
    private List<Long> extractUserIds(Page<UserLog> userLogs) {
        return userLogs.getContent().stream()
                .map(UserLog::getUserId)
                .distinct()
                .toList();
    }

    /**
     * UserInfoDto에서 userId 리스트 추출
     */
    private List<Long> extractUserIds(List<UserInfoDto> users) {
        return users.stream()
                .map(UserInfoDto::userId)
                .distinct()
                .toList();
    }
}
