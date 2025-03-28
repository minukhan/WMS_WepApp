package hyundai.partservice.app.inventory.application.service;

import hyundai.partservice.app.inventory.adapter.dto.InventorySectionDto;
import hyundai.partservice.app.inventory.adapter.dto.InventoryStorageResponse;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.in.InventoryFindByPartIdsUseCase;
import hyundai.partservice.app.inventory.application.port.out.InventoryFindByPartIdsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryFindByPartIdsService implements InventoryFindByPartIdsUseCase {

    private final InventoryFindByPartIdsPort inventoryFindByPartIdsPort;

    @Override
    public InventoryStorageResponse findByPartIds(List<String> partIds) {

        List<Inventory> inventories = inventoryFindByPartIdsPort.findByPartIds(partIds);

        List<InventorySectionDto> inventorySectionDtos = inventories.stream()
                .map(inventory -> InventorySectionDto.from(inventory))
                .collect(Collectors.toList());

        return InventoryStorageResponse.from(inventorySectionDtos);
    }
}
