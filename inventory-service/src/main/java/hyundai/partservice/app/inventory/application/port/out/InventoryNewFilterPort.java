package hyundai.partservice.app.inventory.application.port.out;

import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface InventoryNewFilterPort {

    public abstract Page<Inventory> findAllInventory(PageRequest pageRequest);
    public abstract List<Part> findByPartIdContaining(String partId);
    public abstract List<Part> findByPartNameContaining(String partName);
    public abstract List<Section> findBySectionNameContaining(String sectionName);
    public abstract Supplier findBySupplierContaining(String supplierName);
}
