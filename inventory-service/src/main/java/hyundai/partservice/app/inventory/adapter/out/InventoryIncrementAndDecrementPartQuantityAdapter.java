package hyundai.partservice.app.inventory.adapter.out;

import hyundai.partservice.app.infrastructure.repository.InventoryRepository;
import hyundai.partservice.app.infrastructure.repository.PartRepository;
import hyundai.partservice.app.infrastructure.repository.SectionRepository;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.out.InventoryIncrementAndDecrementPartQuantityPort;
import hyundai.partservice.app.inventory.exception.InventorySectionFloorAndSectionNameAndPartIdNotFoundException;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.exception.PartNotFoundException;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.exception.SectionIdNotFoundException;
import hyundai.partservice.app.section.exception.SectionNameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryIncrementAndDecrementPartQuantityAdapter implements InventoryIncrementAndDecrementPartQuantityPort {

    private final InventoryRepository inventoryRepository;
    private final SectionRepository sectionRepository;
    private final PartRepository partRepository;

    @Override
    public Inventory findInventoryBySectionAndPart(String sectionName, int floor, String partId) {

        Inventory inventory = inventoryRepository.findInventory(sectionName,floor,partId).orElse(null);
        return inventory;
    }

    @Override
    public void saveInventory(Inventory inventory) {
        inventoryRepository.save(inventory);
    }

    @Override
    public Section findBySectionId(Long sectionId) {
        return sectionRepository.findById(sectionId).orElseThrow(()->new SectionIdNotFoundException());
    }

    @Override
    public void saveSection(Section section) {
        sectionRepository.save(section);
    }

    @Override
    public Part findByPartId(String partId) {
        return partRepository.findById(partId).orElseThrow(()-> new PartNotFoundException());
    }

    @Override
    public Section findBySectionNameAndFloor(String sectionName, int floor) {
        return sectionRepository.findSectionByNameAndFloor(sectionName,floor).orElseThrow(()->new SectionNameNotFoundException());
    }

    @Override
    public void savePart(Part part) {
        partRepository.save(part);
    }


}
