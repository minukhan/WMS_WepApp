package hyundai.purchaseservice.purchase.application.port.in;

import hyundai.purchaseservice.purchase.adapter.in.dto.PurchaseDetailResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.PurchaseRequestListResponse;

public interface SearchRequestUseCase {
    PurchaseRequestListResponse searchPurchaseRequests(
            Integer page, Integer size, String type, String searchType, String searchText, String orderType, Boolean isOrderByDesc);

    PurchaseDetailResponse searchPurchaseDetails(Long requestId);
}
