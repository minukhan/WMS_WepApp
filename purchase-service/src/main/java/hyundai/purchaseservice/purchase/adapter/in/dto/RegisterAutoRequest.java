package hyundai.purchaseservice.purchase.adapter.in.dto;

import hyundai.purchaseservice.purchase.adapter.out.dto.PartIdAndQuantityResponse;

import java.util.List;

public record RegisterAutoRequest(
        List<PartIdAndQuantityResponse> requests
) {
}
