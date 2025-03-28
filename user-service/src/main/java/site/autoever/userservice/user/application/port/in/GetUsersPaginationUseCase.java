package site.autoever.userservice.user.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.autoever.userservice.user.application.dto.UserInfoDto;

public interface GetUsersPaginationUseCase {
    Page<UserInfoDto> getAllUsers(Pageable pageable);
    Page<UserInfoDto> getUsersByName(String name, Pageable pageable);
    Page<UserInfoDto> getUsersByRole(String role, Pageable pageable);
    Page<UserInfoDto> getUserByPhoneNumber(String phoneNumber, Pageable pageable);
    Page<UserInfoDto> getUserByEmail(String email, Pageable pageable);
}
