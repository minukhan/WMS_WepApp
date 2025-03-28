package site.autoever.userservice.user.application.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import site.autoever.userservice.user.application.dto.UserInfoDto;
import site.autoever.userservice.user.application.entity.User;
import site.autoever.userservice.user.application.port.in.GetUsersUseCase;
import site.autoever.userservice.user.application.port.out.GetUserPort;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetUsersService implements GetUsersUseCase {

    private final GetUserPort getUserPort;

    @Override
    public List<UserInfoDto> getUsersByIds(List<Long> userIds) {
        List<User> users = getUserPort.getUsersByIds(userIds);

        return users.stream()
                .map(UserInfoDto::of)
                .toList();
    }

    @Override
    public List<UserInfoDto> getUsersByName(String name) {
        List<User> users = getUserPort.getUsersByName(name);

        return users.stream()
                .map(UserInfoDto::of)
                .toList();
    }

    @Override
    public List<UserInfoDto> getUsersByEmail(String email) {
        List<User> users = getUserPort.getUsersByEmail(email);

        return users.stream()
                .map(UserInfoDto::of)
                .toList();
    }

    @Override
    public List<UserInfoDto> getUsersByRole(String role) {
        List<User> users = getUserPort.getUsersByRole(role);

        return users.stream()
                .map(UserInfoDto::of)
                .toList();
    }

    @Override
    public List<UserInfoDto> getUserByPhoneNumber(String phoneNumber) {
        List<User> users = getUserPort.getUsersByPhoneNumber(phoneNumber);

        return users.stream()
                .map(UserInfoDto::of)
                .toList();
    }

}
