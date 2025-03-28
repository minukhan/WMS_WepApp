package hyundai.partservice.app.inventory.adapter.out;


import hyundai.partservice.app.infrastructure.repository.InventoryRepository;
import hyundai.partservice.app.infrastructure.repository.PartRepository;
import hyundai.partservice.app.infrastructure.repository.SectionRepository;
import hyundai.partservice.app.infrastructure.repository.SupplierRepositroy;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.out.InventoryFilterPort;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.exception.PartNameNotFoundException;
import hyundai.partservice.app.part.exception.PartNotFoundException;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.exception.SectionNameNotFoundException;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import hyundai.partservice.app.supplier.exception.SupplierNameNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
@RequiredArgsConstructor
public class InventoryFilterAdapter implements InventoryFilterPort {

    private final PartRepository partRepository;
    private final SectionRepository sectionRepository;
    private final SupplierRepositroy supplierRepository;
    private final InventoryRepository inventoryRepository;
    @Override
    public List<Inventory> findAllInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    public Part findByPartId(String partId) {

        Part part = partRepository.findByIdWithInventoryList(partId).orElseThrow(()->new PartNotFoundException());

        return part;
    }

    @Override
    public Part findByName(String partName) {

        Part part = partRepository.findByName(partName).orElseThrow(()->new PartNameNotFoundException());

        return part;
    }

    @Override
    public Section findBySectionName(String sectionName) {

        Section section = sectionRepository.findByName(sectionName).orElseThrow(()->new SectionNameNotFoundException());
        log.info("######################" + section);
        return section;
    }

    @Override
    public Supplier findBySupplierName(String supplierName) {

        Supplier supplier = supplierRepository.findByName(supplierName);

        if(supplier == null) {
            throw new SupplierNameNotFoundException();
        }

        return supplier;
    }
}
