package site.autoever.logservice.user_log.adapter.out;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.autoever.logservice.infrastructure.repository.LogJpaRepository;
import site.autoever.logservice.user_log.application.entity.UserLog;
import site.autoever.logservice.user_log.application.port.out.SaveLogPort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SaveLogAdapter implements SaveLogPort {

    private final LogJpaRepository logJpaRepository;

    @Override
    public Long saveLog(UserLog userLog) {
        return logJpaRepository.save(userLog).getId();
    }

    @Override
    public List<UserLog> saveAllLogs(List<UserLog> userLogList) {
        return logJpaRepository.saveAll(userLogList);
    }
}
