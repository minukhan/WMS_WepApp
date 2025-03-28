package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

public record SectionDto(
        Long sectionId,
        String sectionName,
        Integer quantity,
        Integer maxCapacity,
        Integer floor
) {
}
