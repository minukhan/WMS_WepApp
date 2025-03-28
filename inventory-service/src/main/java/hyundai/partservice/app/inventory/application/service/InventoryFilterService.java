package hyundai.partservice.app.inventory.application.service;

import hyundai.partservice.app.inventory.adapter.dto.InventoryFilterResponse;
import hyundai.partservice.app.inventory.adapter.dto.InventoryPartSupplierSectionDto;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.in.InventoryFilterUseCase;
import hyundai.partservice.app.inventory.application.port.out.InventoryFilterPort;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryFilterService implements InventoryFilterUseCase {

    private final InventoryFilterPort inventoryFilterPort;

    @Override
    public InventoryFilterResponse filter(
            String partId, String partName, String sectionName, String supplierName,
            String currentQuantity, String safetyQuantity, Pageable pageable) {

        List<Inventory> inventories = inventoryFilterPort.findAllInventory();
        inventories.sort(Comparator.comparing(Inventory::getId).reversed());

        // 필터링 조건 적용
        if (partId != null && !partId.isEmpty()) {
            Part part = inventoryFilterPort.findByPartId(partId);
            inventories = part.getInventoryList();
        } else if (partName != null && !partName.isEmpty()) {
            Part part = inventoryFilterPort.findByName(partName);
            inventories = part.getInventoryList();
        } else if (sectionName != null && !sectionName.isEmpty()) {
            Section section = inventoryFilterPort.findBySectionName(sectionName);
            inventories = section.getInventories();
        } else if (supplierName != null && !supplierName.isEmpty()) {
            Supplier supplier = inventoryFilterPort.findBySupplierName(supplierName);
            inventories = supplier.getParts().stream()
                    .map(Part::getInventoryList)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }

        // DTO 변환
        List<InventoryPartSupplierSectionDto> inventoryDtos = inventories.stream()
                .map(InventoryPartSupplierSectionDto::from)
                .collect(Collectors.toList());

        // 정렬 적용
        if (currentQuantity != null && !currentQuantity.isEmpty()) {
            inventoryDtos.sort(currentQuantity.equalsIgnoreCase("DESC")
                    ? Comparator.comparing(InventoryPartSupplierSectionDto::quantity).reversed()
                    : Comparator.comparing(InventoryPartSupplierSectionDto::quantity));
        } else if (safetyQuantity != null && !safetyQuantity.isEmpty()) {
            inventoryDtos.sort(safetyQuantity.equalsIgnoreCase("DESC")
                    ? Comparator.comparing(InventoryPartSupplierSectionDto::safetyQuantity).reversed()
                    : Comparator.comparing(InventoryPartSupplierSectionDto::safetyQuantity));
        }

        // 페이지네이션 적용
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), inventoryDtos.size());
        List<InventoryPartSupplierSectionDto> pagedList = inventoryDtos.subList(start, end);

        // InventoryFilterResponse 객체 생성 후 반환
        return new InventoryFilterResponse(
                pagedList,
                pageable.getPageNumber(),
                (int) Math.ceil((double) inventoryDtos.size() / pageable.getPageSize()),
                inventoryDtos.size(),
                pageable.getPageNumber() == 0,
                (pageable.getPageNumber() + 1) * pageable.getPageSize() >= inventoryDtos.size()
        );
    }

}
