package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

import java.util.List;

public record InventoryListByPartIdDto(
        PartDto partDto,
        List<InventorySectionDto> inventoryDtoList
) {
}
