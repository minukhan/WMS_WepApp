package site.autoever.reportservice.report.application.dto.alarm;

public record AlarmCreateRequest(
        String role,
        String message,
        String type
) {
}
