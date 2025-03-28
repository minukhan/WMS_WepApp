package site.autoever.logservice.user_log.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.autoever.logservice.user_log.application.dto.UserLogInfoDto;

import java.time.LocalDate;

public interface GetAllLogsUseCase {
    Page<UserLogInfoDto> getAllLogs(Pageable pageable);
    Page<UserLogInfoDto> getAllLogsByName(String name, Pageable pageable);
    Page<UserLogInfoDto> getAllLogsByRole(String role, Pageable pageable);
    Page<UserLogInfoDto> getAllLogsByMessage(String message, Pageable pageable);
    Page<UserLogInfoDto> getAllLogsByEmail(String email, Pageable pageable);
    Page<UserLogInfoDto> getAllLogsByPhoneNumber(String phoneNumber, Pageable pageable);
    Page<UserLogInfoDto> getAllLogsByDate(LocalDate date, Pageable pageable);
}
