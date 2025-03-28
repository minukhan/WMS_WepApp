package hyundai.partservice.app.inventory.application.port.out;


import hyundai.partservice.app.inventory.application.entity.Inventory;

import java.util.List;

public interface InventoryFindByPartIdsPort {
    public abstract List<Inventory> findByPartIds(List<String> partIds);
}
