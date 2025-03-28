package site.autoever.reportservice.report.application.dto.log;

public record LogCreateRequest(
        long userId,
        String message
) {

}
