package hyundai.safeservice.app.propriety_Stock.adapter.dto.fein;

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
}

