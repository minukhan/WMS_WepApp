package hyundai.autostockservice.autoStock.adapter.dto.feign;

public record PartIdAndQuantityDto(
        String partId,
        Integer quantity
) {
}
