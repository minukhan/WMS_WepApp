package site.autoever.userservice.user.adapter.in.dto;

public record CreateTempUserResponse(
        String tempUserId,
        long managerId,
        String role
) {
}
