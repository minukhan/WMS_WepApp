package hyundai.storageservice.app.storage_schedule.adapter.dto.fein;

import java.util.List;

public record PartInventoryResponse(

        PartDto partDto,
        List<InventorySectionDto> inventoryDtoList
) {

}
