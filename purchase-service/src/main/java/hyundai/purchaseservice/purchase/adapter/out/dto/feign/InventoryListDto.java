package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

import java.util.List;

public record InventoryListDto(
        List<InventoryDto> inventoryDtos
) {
}
