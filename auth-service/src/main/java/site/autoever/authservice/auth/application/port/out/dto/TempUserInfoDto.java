package site.autoever.authservice.auth.application.port.out.dto;

public record TempUserInfoDto(
        String tempUserId,
        long managerId,
        String role
) {
}