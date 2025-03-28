package hyundai.partservice.app.inventory.application.service;

import hyundai.partservice.app.inventory.adapter.dto.InventoryDto;
import hyundai.partservice.app.inventory.adapter.dto.InventoryResponse;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.in.InventoryFindByIdUseCase;
import hyundai.partservice.app.inventory.application.port.out.InventoryFindByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryFindByIdService implements InventoryFindByIdUseCase {

    private final InventoryFindByIdPort inventoryFindByIdPort;

    @Override
    public InventoryResponse findById(Long id) {

        Inventory inventory = inventoryFindByIdPort.findById(id);

        InventoryDto  inventoryDto= InventoryDto.from(inventory);

        return InventoryResponse.from(inventoryDto);
    }
}
