package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

import java.util.List;

public record ExpectedStockResponse(
        Integer total,
        List<PartQuantityDto> parts
) {
}
