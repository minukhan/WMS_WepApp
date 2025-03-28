package site.autoever.userservice.user.adapter.in.dto;

public record CreateTempUserRequest(
        String tempUserId,
        long managerId
) {
}
