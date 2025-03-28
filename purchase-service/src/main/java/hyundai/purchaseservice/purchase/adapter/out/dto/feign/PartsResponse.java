package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

import java.util.List;

public record PartsResponse(
        List<PartDto> content,
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages,
        Boolean last
) {
}
