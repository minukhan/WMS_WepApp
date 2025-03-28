package site.autoever.logservice.user_log.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.autoever.logservice.user_log.application.entity.UserLog;
import site.autoever.logservice.user_log.application.port.in.SaveLogUseCase;
import site.autoever.logservice.user_log.application.port.out.SaveLogPort;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaveLogService implements SaveLogUseCase {

    private final SaveLogPort saveLogPort;

    @Override
    @Transactional
    public void saveLog(UserLog userLog) {
        Long saveId = saveLogPort.saveLog(userLog);

        log.info("Save user log: {}", saveId);
    }
}
