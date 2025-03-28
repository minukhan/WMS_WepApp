package hyundai.partservice.app.inventory.application.service;


import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.in.InventoryDecrementPartQuantityUseCase;
import hyundai.partservice.app.inventory.application.port.out.InventoryIncrementAndDecrementPartQuantityPort;
import hyundai.partservice.app.inventory.exception.InventoryPartQuantityEmptyException;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.section.application.entity.Section;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class InventoryDecrementPartQuantityService implements InventoryDecrementPartQuantityUseCase {

    private final InventoryIncrementAndDecrementPartQuantityPort inventoryDecrementPartQuantityPort;

    @Override
    public void decrementPartQuantity(String partId, String sectionName, int floor) {

        Inventory inventory = inventoryDecrementPartQuantityPort.findInventoryBySectionAndPart(sectionName, floor, partId);

        if (inventory.getPartQuantity() <= 0) {
            throw new InventoryPartQuantityEmptyException();
        }

        //  부품 (Part) 정보 가져오기
        Part associatedPart = inventory.getPart();

        //  Section 정보 가져오기
        Section section = inventoryDecrementPartQuantityPort.findBySectionId(inventory.getSection().getId());

        //  각 수량 감소 처리
        inventory.updatePartQuantity(inventory.getPartQuantity() - 1);
        associatedPart.updateQuantity(associatedPart.getQuantity() - 1);
        section.updateQuantity(section.getQuantity() - 1);

        //  업데이트된 데이터 저장
        inventoryDecrementPartQuantityPort.saveInventory(inventory);
        inventoryDecrementPartQuantityPort.savePart(associatedPart);
        inventoryDecrementPartQuantityPort.saveSection(section);
    }
}
