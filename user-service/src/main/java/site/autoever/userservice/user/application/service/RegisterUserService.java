package site.autoever.userservice.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.autoever.userservice.user.application.dto.UserDetailDto;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.port.in.RegisterUserUseCase;
import site.autoever.userservice.user.application.port.out.GetUserPort;
import site.autoever.userservice.user.application.port.out.RegisterUserPort;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    private final RegisterUserPort registerUserPort;
    private final GetUserPort getUserPort;

    @Override
    public UserDetailDto registerUser(User user) {
        Optional<User> existingUser = getUserPort.getUserByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            return UserDetailDto.of(existingUser.get());
        }

        return UserDetailDto.of(registerUserPort.saveUser(user));
    }
}
