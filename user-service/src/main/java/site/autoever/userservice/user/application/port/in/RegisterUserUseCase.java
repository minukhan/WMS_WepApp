package site.autoever.userservice.user.application.port.in;

import site.autoever.userservice.user.application.dto.UserDetailDto;
import site.autoever.userservice.user.application.entity.User;

public interface RegisterUserUseCase {
    UserDetailDto registerUser(User user);
}
