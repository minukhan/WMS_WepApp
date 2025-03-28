package site.autoever.userservice.user.application.dto;

import site.autoever.userservice.user.application.entity.User;

public record ClientUserInfoDto(
        UserInfoDto userInfoDto,
        boolean verified
) {
    public static ClientUserInfoDto of(User user) {
        return new ClientUserInfoDto(
                UserInfoDto.of(user),
                !user.getRole().equals("ROLE_USER")
        );
    }
}
