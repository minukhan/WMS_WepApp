package site.autoever.userservice.user.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import site.autoever.userservice.infrastructure.repository.UserJpaRepository;
import site.autoever.userservice.user.application.dto.UserInfoDto;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.port.out.GetUserPort;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetUserAdapter implements GetUserPort {

    private final UserJpaRepository repository;

    @Override
    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public List<User> getUsersByEmail(String email) {
        return repository.findAllByEmail(email);
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return repository.findAllByRole(role);
    }

    @Override
    public List<User> getUsersByPhoneNumber(String phoneNumber) {
        return repository.findAllByPhoneNumber(phoneNumber);
    }

    @Override
    public List<User> getUsersByName(String name) {
        return repository.findAllByName(name);
    }

    @Override
    public List<User> getUsersByIds(List<Long> ids) {
        return repository.findAllById(ids);
    }

}
