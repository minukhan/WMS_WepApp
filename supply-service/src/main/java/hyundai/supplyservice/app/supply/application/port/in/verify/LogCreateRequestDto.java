package hyundai.supplyservice.app.supply.application.port.in.verify;

public record LogCreateRequestDto(
        Long userId,
        String message
) {
}
