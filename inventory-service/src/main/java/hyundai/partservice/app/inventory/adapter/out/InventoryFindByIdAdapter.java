package hyundai.partservice.app.inventory.adapter.out;


import hyundai.partservice.app.infrastructure.repository.InventoryRepository;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.out.InventoryFindByIdPort;
import hyundai.partservice.app.inventory.exception.InventoryIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryFindByIdAdapter implements InventoryFindByIdPort {

    private final InventoryRepository inventoryRepository;

    @Override
    public Inventory findById(Long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(()-> new InventoryIdNotFoundException());
        return inventory;
    }
}
