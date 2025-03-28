package site.autoever.logservice.user_log.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.autoever.logservice.user_log.application.entity.UserLog;

import java.time.LocalDate;
import java.util.List;

public interface GetLogsPort {
    Page<UserLog> getLogs(Pageable pageable);
    Page<UserLog> getLogsByUserIds(List<Long> userIds, Pageable pageable);
    Page<UserLog> getLogsByMessage(String message, Pageable pageable);
    Page<UserLog> getLogsByDate(LocalDate date, Pageable pageable);

}
