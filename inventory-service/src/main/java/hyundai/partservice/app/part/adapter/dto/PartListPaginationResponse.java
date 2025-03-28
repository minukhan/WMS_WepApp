package hyundai.partservice.app.part.adapter.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PartListPaginationResponse(
        List<PartDto> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
)
{
    public static PartListPaginationResponse from(Page<PartDto> partPage) {
        return new PartListPaginationResponse(
                partPage.getContent(),
                partPage.getNumber(),
                partPage.getSize(),
                partPage.getTotalElements(),
                partPage.getTotalPages(),
                partPage.isLast()
        );
    }
}
