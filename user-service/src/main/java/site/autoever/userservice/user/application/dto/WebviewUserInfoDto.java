package site.autoever.userservice.user.application.dto;

import site.autoever.userservice.user.application.entity.User;

public record WebviewUserInfoDto(
        UserInfoDto userInfoDto,
        boolean isManager
) {
    public static WebviewUserInfoDto of(User user) {
        return new WebviewUserInfoDto(
                UserInfoDto.of(user),
                user.getRole().equals("ROLE_WAREHOUSE_MANAGER") || user.getRole().equals("ROLE_ADMIN")
        );
    }

}
