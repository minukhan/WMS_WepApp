package hyundai.purchaseservice.purchase.adapter.out.dto;

import java.util.List;

public record GetScheduleDto(
        String searchType,
        String searchText,
        List<String> partIds,
        List<Long> sectionIds,
        String orderType,
        String orderBy
) {
}
