package hyundai.supplyservice.app.supply.application.port.in.commondto;

public record AlarmCreateRequestDto(
        String role,
        String message,
        String type
) {
}
