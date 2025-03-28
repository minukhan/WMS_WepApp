package hyundai.partservice.app.inventory.application.port.out;


import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.supplier.application.entity.Supplier;

import java.util.List;

public interface InventoryFilterPort {
    public abstract List<Inventory> findAllInventory();
    public abstract Part findByPartId(String partId);
    public abstract Part findByName(String partName);
    public abstract Section findBySectionName(String sectionName);
    public abstract Supplier findBySupplierName(String supplierName);
}
