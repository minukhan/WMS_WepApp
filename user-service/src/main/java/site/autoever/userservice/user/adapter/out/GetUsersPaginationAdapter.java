package site.autoever.userservice.user.adapter.out;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import site.autoever.userservice.infrastructure.repository.UserJpaRepository;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.port.out.GetUsersPaginationPort;

@Component
@RequiredArgsConstructor
public class GetUsersPaginationAdapter implements GetUsersPaginationPort {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userJpaRepository.findAll(pageable);
    }

    @Override
    public Page<User> getUsersByRole(String role, Pageable pageable) {
        return userJpaRepository.findAllByRole(role, pageable);
    }

    @Override
    public Page<User> getUsersByPhoneNumber(String phoneNumber, Pageable pageable) {
        return userJpaRepository.findAllByPhoneNumber(phoneNumber, pageable);
    }

    @Override
    public Page<User> getUsersByName(String name, Pageable pageable) {
        return userJpaRepository.findAllByName(name, pageable);
    }

    @Override
    public Page<User> getUsersByEmail(String email, Pageable pageable) {
        return userJpaRepository.findAllByEmail(email, pageable);
    }


}
