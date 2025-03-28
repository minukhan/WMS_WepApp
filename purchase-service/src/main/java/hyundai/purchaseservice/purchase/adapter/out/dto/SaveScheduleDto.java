package hyundai.purchaseservice.purchase.adapter.out.dto;

import java.time.LocalDate;

public record SaveScheduleDto(
        Long requestId,
        Integer requestedQuantity,
        Integer processedQuantity,
        LocalDate scheduledAt,
        String status,
        Long sectionId
) {
    public static SaveScheduleDto of(SaveRequestDto request, Long sectionId, Integer requestedQuantity) {
        return new SaveScheduleDto(
                request.getId(),
                requestedQuantity,
                0,
                request.getExpectedAt(),
                request.getStatus(),
                sectionId
        );
    }
}
