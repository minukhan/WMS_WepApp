package site.autoever.logservice.user_log.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import site.autoever.logservice.infrastructure.repository.LogJpaRepository;
import site.autoever.logservice.user_log.application.entity.UserLog;
import site.autoever.logservice.user_log.application.port.out.GetLogsPort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetLogsAdapter implements GetLogsPort {

    private final LogJpaRepository logJpaRepository;

    @Override
    public Page<UserLog> getLogs(Pageable pageable) {
        return logJpaRepository.findAll(pageable);
    }

    @Override
    public Page<UserLog> getLogsByUserIds(List<Long> userIds, Pageable pageable) {
        return logJpaRepository.findByUserIdIn(userIds, pageable);
    }

    @Override
    public Page<UserLog> getLogsByMessage(String message, Pageable pageable) {
        return logJpaRepository.findByMessageContaining(message, pageable);
    }

    @Override
    public Page<UserLog> getLogsByDate(LocalDate date, Pageable pageable) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return logJpaRepository.findByCreatedAtBetween(startOfDay, endOfDay, pageable);
    }

}
