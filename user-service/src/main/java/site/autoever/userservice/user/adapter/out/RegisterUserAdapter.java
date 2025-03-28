package site.autoever.userservice.user.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.autoever.userservice.infrastructure.repository.UserJpaRepository;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.port.out.RegisterUserPort;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RegisterUserAdapter implements RegisterUserPort {

    private final UserJpaRepository repository;

    @Override
    public User saveUser(User user) {
        return repository.save(user);
    }

}
