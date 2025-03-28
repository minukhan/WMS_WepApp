package site.autoever.userservice.user.application.port.out;

import site.autoever.userservice.user.application.entity.User;

import java.util.Optional;

public interface RegisterUserPort {
    User saveUser(User user);
}
