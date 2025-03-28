package site.autoever.authservice.auth.application.dto;

public record UserSummaryDto(
        long userId,
        String role
) {
}
