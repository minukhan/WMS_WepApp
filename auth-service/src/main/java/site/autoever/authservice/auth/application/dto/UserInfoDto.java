package site.autoever.authservice.auth.application.dto;

public record UserInfoDto(
        String sub,                         // 사용자 ID (Subject)
        String upn,                         // 사용자 이름
        boolean emailVerified,
        String role,                        // 사용자 역할
        String phoneNumber,
        String preferredUsername,
        String email,                       // 이메일 주소
        String customAddress
) {
}
