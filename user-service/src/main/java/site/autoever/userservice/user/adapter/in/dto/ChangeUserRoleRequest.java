package site.autoever.userservice.user.adapter.in.dto;

public record ChangeUserRoleRequest(
        long userId,
        String role
) {
}
