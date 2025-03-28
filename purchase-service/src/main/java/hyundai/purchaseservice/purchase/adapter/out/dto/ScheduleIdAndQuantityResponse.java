package hyundai.purchaseservice.purchase.adapter.out.dto;

public record ScheduleIdAndQuantityResponse(
        Long id,
        Long requestId,
        Integer processedQuantity
) {
}
