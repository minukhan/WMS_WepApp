package hyundai.partservice.app.inventory.adapter.out;


import hyundai.partservice.app.infrastructure.repository.InventoryRepository;
import hyundai.partservice.app.infrastructure.repository.PartRepository;
import hyundai.partservice.app.infrastructure.repository.SectionRepository;
import hyundai.partservice.app.infrastructure.repository.SupplierRepositroy;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.out.InventoryNewFilterPort;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.exception.PartNameNotFoundException;
import hyundai.partservice.app.part.exception.PartNotFoundException;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.exception.SectionNameNotFoundException;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import hyundai.partservice.app.supplier.exception.SupplierNameNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class InventoryNewFilterAdapter implements InventoryNewFilterPort {

    private final InventoryRepository inventoryRepository;
    private final PartRepository partRepository;
    private final SectionRepository sectionRepository;
    private final SupplierRepositroy supplierRepository;

    @Override
    public Page<Inventory> findAllInventory(PageRequest pageRequest) {

        return inventoryRepository.findAll(pageRequest);
    }

    @Override
    public List<Part> findByPartIdContaining(String partId) {
        List<Part> parts = partRepository.findByIdContaining(partId);


        log.info("########################"+parts.size());

        if (parts.isEmpty()) {
            throw new PartNotFoundException();
        }
        return parts; // 첫 번째 일치하는 요소 반환
    }

    @Override
    public List<Part> findByPartNameContaining(String partName) {
        List<Part> parts = partRepository.findByNameContainingWithSupplier(partName);
        if (parts.isEmpty()) {
            throw new PartNameNotFoundException();
        }
        return parts;
    }

    @Override
    public List<Section> findBySectionNameContaining(String sectionName) {

        List<Section> sections = sectionRepository.findByNameContaining(sectionName);

        if(sections.isEmpty()){
            throw new SectionNameNotFoundException();
        }

        return sections;
    }

    @Override
    public Supplier findBySupplierContaining(String supplierName) {

        return supplierRepository.findByName(supplierName);
    }

}
