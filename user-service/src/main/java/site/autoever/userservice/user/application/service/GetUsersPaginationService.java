package site.autoever.userservice.user.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.autoever.userservice.user.application.dto.UserInfoDto;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.port.in.GetUsersPaginationUseCase;
import site.autoever.userservice.user.application.port.out.GetUsersPaginationPort;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetUsersPaginationService implements GetUsersPaginationUseCase {

    private final GetUsersPaginationPort getUsersPaginationPort;


    @Override
    public Page<UserInfoDto> getAllUsers(Pageable pageable) {
        Page<User> users = getUsersPaginationPort.getAllUsers(pageable);

        return users.map(UserInfoDto::of);
    }

    @Override
    public Page<UserInfoDto> getUsersByName(String name, Pageable pageable) {
        log.info("name : {}", name);
        Page<User> userPages = getUsersPaginationPort.getUsersByName(name, pageable);

        return userPages.map(UserInfoDto::of);
    }

    @Override
    public Page<UserInfoDto> getUsersByRole(String role, Pageable pageable) {
        log.info("role : {}", role);

        Page<User> userPages = getUsersPaginationPort.getUsersByRole(role, pageable);

        return userPages.map(UserInfoDto::of);
    }

    @Override
    public Page<UserInfoDto> getUserByPhoneNumber(String phoneNumber, Pageable pageable) {
        log.info("phone : {}", phoneNumber);

        Page<User> userPages = getUsersPaginationPort.getUsersByPhoneNumber(phoneNumber, pageable);

        return userPages.map(UserInfoDto::of);
    }

    @Override
    public Page<UserInfoDto> getUserByEmail(String email, Pageable pageable) {
        log.info("email : {}", email);

        Page<User> userPages = getUsersPaginationPort.getUsersByEmail(email, pageable);

        return userPages.map(UserInfoDto::of);
    }
}
