package site.autoever.authservice.auth.application.port.out.dto;

public record TempUserRegisterRequest(
        String tempUserId,
        long managerId
) {
}
