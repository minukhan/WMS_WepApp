package hyundai.purchaseservice.purchase.adapter.in.dto;

import hyundai.purchaseservice.purchase.application.dto.PageInfo;
import hyundai.purchaseservice.purchase.application.dto.PurchaseRequestInfoDto;

import java.util.List;

public record PurchaseRequestListResponse(
        List<PurchaseRequestInfoDto> purchaseRequestList,
        PageInfo pageInfo
) {
    public static PurchaseRequestListResponse of(List<PurchaseRequestInfoDto> purchaseRequestList, Integer page, Integer size, Long totalCount) {
        int totalPages = (int)Math.ceil((double) totalCount / size);
        Boolean hasNext = page != totalPages;
        Boolean hasPrevious = page != 1;

        PageInfo pageInfo = new PageInfo(page, size, totalPages, totalCount, hasNext, hasPrevious );

        return new PurchaseRequestListResponse(
                purchaseRequestList,
                pageInfo
        );
    }
}
