package site.autoever.logservice.user_log.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.autoever.logservice.infrastructure.util.AsyncLogProcessor;
import site.autoever.logservice.user_log.application.entity.UserLog;
import site.autoever.logservice.user_log.application.port.in.AsyncSaveLogUseCase;


@Service
@RequiredArgsConstructor
public class AsyncSaveLogService implements AsyncSaveLogUseCase {

    private final AsyncLogProcessor logProcessor;
    @Override
    public void saveLog(UserLog userLog) {
        logProcessor.enqueueLog(userLog);
    }
}
