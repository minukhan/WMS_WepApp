package site.autoever.userservice.user.application.dto;

import site.autoever.userservice.user.application.entity.User;

import java.time.LocalDateTime;

public record UserInfoDto(
        long userId,
        String address,
        String email,
        String name,
        String phoneNumber,
        String role,
        LocalDateTime createdAt
) {
    public static UserInfoDto of(User user) {
        return new UserInfoDto(
                user.getId(),
                user.getAddress(),
                user.getEmail(),
                user.getName(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
