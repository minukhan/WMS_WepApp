package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

import java.util.List;

public record SectionListDto(
        List<SectionDto> sectionDtos
) {
}
