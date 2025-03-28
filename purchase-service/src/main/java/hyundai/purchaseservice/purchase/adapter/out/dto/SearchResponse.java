package hyundai.purchaseservice.purchase.adapter.out.dto;

import java.time.LocalDate;

public record SearchResponse(
        Long id,
        String partId,
        Integer quantity,
        String requesterName,
        LocalDate orderedAt,
        LocalDate deadline,
        Long totalPrice
) {
}
