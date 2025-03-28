package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

public record SSERequest(
        String sectionName,
        Integer floor,
        Boolean isAdd
) {
}
