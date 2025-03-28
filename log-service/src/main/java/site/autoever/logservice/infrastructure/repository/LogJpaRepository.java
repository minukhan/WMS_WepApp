package site.autoever.logservice.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.autoever.logservice.user_log.application.entity.UserLog;

import java.time.LocalDateTime;
import java.util.List;

public interface LogJpaRepository extends JpaRepository<UserLog, Long> {
    Page<UserLog> findAll(Pageable pageable);
    Page<UserLog> findByUserIdIn(List<Long> userIds, Pageable pageable);
    Page<UserLog> findByMessageContaining(String message, Pageable pageable);
    Page<UserLog> findByCreatedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);
}
