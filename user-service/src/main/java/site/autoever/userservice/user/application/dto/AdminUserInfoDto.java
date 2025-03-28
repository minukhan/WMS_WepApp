package site.autoever.userservice.user.application.dto;

import site.autoever.userservice.user.application.entity.User;

public record AdminUserInfoDto(
        UserInfoDto userInfoDto,
        boolean isAdmin
) {
    public static AdminUserInfoDto of(User user) {
        return new AdminUserInfoDto(
                UserInfoDto.of(user),
                user.getRole().equals("ROLE_ADMIN") || user.getRole().equals("ROLE_INFRA_MANAGER")
        );
    }

}
