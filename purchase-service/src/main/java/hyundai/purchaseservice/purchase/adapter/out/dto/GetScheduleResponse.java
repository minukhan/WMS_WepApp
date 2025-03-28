package hyundai.purchaseservice.purchase.adapter.out.dto;

import java.time.LocalDate;

public record GetScheduleResponse(
        String partId,
        Integer totalQuantity,
        Integer requestedQuantity,
        Long totalPrice,
        LocalDate scheduledAt,
        Long sectionId
) {
}
