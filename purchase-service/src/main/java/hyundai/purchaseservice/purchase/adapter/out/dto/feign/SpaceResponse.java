package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

public record SpaceResponse(
        Integer currentCount,
        Integer totalCount,
        Integer emptySpace,
        Double persent
) {
}
