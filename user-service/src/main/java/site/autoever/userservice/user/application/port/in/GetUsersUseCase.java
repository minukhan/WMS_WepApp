package site.autoever.userservice.user.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.autoever.userservice.user.application.dto.UserInfoDto;

import java.util.List;

public interface GetUsersUseCase {
    List<UserInfoDto> getUsersByIds(List<Long> userIds);
    List<UserInfoDto> getUsersByName(String name);
    List<UserInfoDto> getUsersByRole(String role);
    List<UserInfoDto> getUsersByEmail(String email);
    List<UserInfoDto> getUserByPhoneNumber(String phoneNumber);
}
