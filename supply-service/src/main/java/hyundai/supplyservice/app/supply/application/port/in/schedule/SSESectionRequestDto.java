package hyundai.supplyservice.app.supply.application.port.in.schedule;

public record SSESectionRequestDto(
        String sectionName,
        int floor,
        boolean isAdd
) {
}
