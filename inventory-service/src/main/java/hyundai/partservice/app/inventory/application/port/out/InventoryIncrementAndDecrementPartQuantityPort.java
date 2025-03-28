package hyundai.partservice.app.inventory.application.port.out;

import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.section.application.entity.Section;

public interface InventoryIncrementAndDecrementPartQuantityPort {
    public abstract Inventory findInventoryBySectionAndPart(String sectionName, int floor, String partId);
    public abstract void saveInventory(Inventory inventory);
    public abstract Section findBySectionId(Long sectionId);
    public abstract void saveSection(Section section);
    public abstract Part findByPartId(String partId);
    public abstract Section findBySectionNameAndFloor(String sectionName, int floor);
    public abstract void savePart(Part part);
}
