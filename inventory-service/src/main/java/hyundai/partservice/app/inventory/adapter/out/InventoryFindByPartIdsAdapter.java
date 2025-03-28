package hyundai.partservice.app.inventory.adapter.out;

import hyundai.partservice.app.infrastructure.repository.InventoryRepository;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.out.InventoryFindByPartIdsPort;
import hyundai.partservice.app.inventory.exception.InventoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InventoryFindByPartIdsAdapter implements InventoryFindByPartIdsPort {

    private final InventoryRepository inventoryRepository;

    @Override
    public List<Inventory> findByPartIds(List<String> partIds) {

        List<Inventory> inventories  = inventoryRepository.findAllByPartIdIn(partIds);

        if(inventories.isEmpty()){
            throw new InventoryNotFoundException();
        }

        return inventories;
    }
}
