package hyundai.partservice.app.inventory.application.port.in;


import hyundai.partservice.app.inventory.adapter.dto.InventoryListResponse;
import hyundai.partservice.app.inventory.adapter.dto.InventoryStorageResponse;

import java.util.List;

public interface InventoryFindByPartIdsUseCase {

    public abstract InventoryStorageResponse findByPartIds(List<String> partIds);
}
