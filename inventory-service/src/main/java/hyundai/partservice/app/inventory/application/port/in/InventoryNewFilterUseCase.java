package hyundai.partservice.app.inventory.application.port.in;

import hyundai.partservice.app.inventory.adapter.dto.InventoryFilterResponse;

public interface InventoryNewFilterUseCase {
    public abstract InventoryFilterResponse filter(int page, int size, String searchType, String searchText, String orderType, String isDesc);
}
