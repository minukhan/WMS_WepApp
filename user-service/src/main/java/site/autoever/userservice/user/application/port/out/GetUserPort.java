package site.autoever.userservice.user.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.autoever.userservice.user.application.dto.UserInfoDto;
import site.autoever.userservice.user.application.entity.User;

import java.util.List;
import java.util.Optional;

public interface GetUserPort {

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);
    List<User> getUsersByEmail(String email);
    List<User> getUsersByRole(String role);

    List<User> getUsersByPhoneNumber(String phoneNumber);

    List<User> getUsersByName(String name);

    List<User> getUsersByIds(List<Long> ids);
}
