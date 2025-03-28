package site.autoever.userservice.user.adapter.in.dto;

import site.autoever.userservice.user.application.entity.User;

import java.time.LocalDate;

public record UserDetailResponse(
        Long id,
        String name,
        String role,
        String phoneNumber,
        String email,
        String address,
        LocalDate createdAt,
        boolean isAdmin
) {
    public static UserDetailResponse from(String currentUserRole, User targetUser) {
        return new UserDetailResponse(
                targetUser.getId(),
                targetUser.getName(),
                targetUser.getRole(),
                targetUser.getPhoneNumber(),
                targetUser.getEmail(),
                targetUser.getAddress(),
                targetUser.getCreatedAt().toLocalDate(),
                currentUserRole.equals("ROLE_ADMIN")
        );
    }

}
