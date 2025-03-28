package hyundai.autostockservice.autoStock.adapter.dto.feign;

import java.util.List;

public record AllPartsResponse(
        List<PartDto> content,
        Integer page,
        Integer size,
        Long totalElements,
        Integer totalPages,
        Boolean last
) {
}
