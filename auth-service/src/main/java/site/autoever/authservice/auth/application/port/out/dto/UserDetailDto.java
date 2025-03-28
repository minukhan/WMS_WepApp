package site.autoever.authservice.auth.application.port.out.dto;

import java.time.LocalDate;

public record UserDetailDto(
        Long id,
        String name,
        String role,
        String phoneNumber,
        String email,
        String address,
        LocalDate createdAt,
        boolean isAdmin
) {
    
}