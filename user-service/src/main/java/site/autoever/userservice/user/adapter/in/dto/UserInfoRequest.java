package site.autoever.userservice.user.adapter.in.dto;

import site.autoever.userservice.user.application.entity.User;

import java.time.LocalDateTime;
import java.time.ZoneId;

public record UserInfoRequest(
        String sub,                         // 사용자 ID (Subject)
        String upn,                         // 사용자 이름
        boolean emailVerified,
        String role,                        // 사용자 역할
        String phoneNumber,
        String preferredUsername,
        String email,                       // 이메일 주소
        String customAddress
) {
    public User toEntity() {
        return User.builder()
                .email(email)
                .address(customAddress)
                .name(preferredUsername)
                .phoneNumber(phoneNumber)
                .role(role)
                .createdAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();
    }
}
