package site.autoever.userservice.user.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.autoever.userservice.user.application.entity.User;

public interface GetUsersPaginationPort {
    Page<User> getAllUsers(Pageable pageable);
    Page<User> getUsersByRole(String role, Pageable pageable);
    Page<User> getUsersByPhoneNumber(String phoneNumber, Pageable pageable);
    Page<User> getUsersByName(String name, Pageable pageable);
    Page<User> getUsersByEmail(String email, Pageable pageable);

}
