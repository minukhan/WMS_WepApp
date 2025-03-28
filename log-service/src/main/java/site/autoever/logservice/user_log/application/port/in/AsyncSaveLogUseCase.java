package site.autoever.logservice.user_log.application.port.in;

import site.autoever.logservice.user_log.application.entity.UserLog;

public interface AsyncSaveLogUseCase {
    void saveLog(UserLog userLog);
}
