package hyundai.supplyservice.app.supply.application.port.in.read;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SupplyRequestInfoDto(
        @Schema(example = "1")
        Long requestId,
        @Schema(example = "고채린")
        String buyer,
        @Schema(description = "요청한 날짜", example="2024-02-10T23:59:59")
        LocalDate requestedAt,
        @Schema(description = "마감날짜", example = "2024-02-20T23:59:59")
        LocalDate deadline,
        @Schema(description = "상태", example = "WAITING")
        String status

) {
}
