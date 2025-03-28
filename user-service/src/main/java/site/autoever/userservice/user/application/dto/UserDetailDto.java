package site.autoever.userservice.user.application.dto;

import site.autoever.userservice.user.application.entity.User;

public record UserDetailDto(
        long userId,
        String role
) {
    public static UserDetailDto of(User user) {
        return new UserDetailDto(
                user.getId(),
                user.getRole()
        );
    }
}
