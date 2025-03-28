package hyundai.purchaseservice.purchase.adapter.out.dto;

public record GetProgressPercentResponse(
    String partId,
    Integer requestedQuantity,
    Integer processedQuantity,
    Long sectionId
){
}
