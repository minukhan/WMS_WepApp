package hyundai.purchaseservice.purchase.adapter.out.dto;

public record GetDayScheduleQuantitesResponse(
        String partId,
        Long quantity,
        Long processedQuantity
) {
}
