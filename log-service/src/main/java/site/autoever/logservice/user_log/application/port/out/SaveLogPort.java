package site.autoever.logservice.user_log.application.port.out;

import site.autoever.logservice.user_log.application.entity.UserLog;

import java.util.List;

public interface SaveLogPort {
    Long saveLog(UserLog userLog);
    List<UserLog> saveAllLogs(List<UserLog> userLogList);
}
