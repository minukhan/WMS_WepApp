package hyundai.supplyservice.app.supply.application.port.in.schedule;

import java.time.LocalDate;
import java.util.List;

public record DailySectionScheduleResponseDto(
        LocalDate date,
        List<PartSectionQuantityDto> parts
) {
}
