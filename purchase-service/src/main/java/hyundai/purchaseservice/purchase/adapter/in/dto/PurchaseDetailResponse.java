package hyundai.purchaseservice.purchase.adapter.in.dto;

import hyundai.purchaseservice.purchase.application.dto.PurchaseDetailItemInfoDto;
import hyundai.purchaseservice.purchase.application.dto.PurchaseDetailOrderInfoDto;

public record PurchaseDetailResponse(
        PurchaseDetailOrderInfoDto orderInfo,
        PurchaseDetailItemInfoDto itemInfo
) {
}
