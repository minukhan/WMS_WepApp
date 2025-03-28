package hyundai.partservice.app.inventory.application.service;

import hyundai.partservice.app.inventory.adapter.dto.InventoryFilterResponse;
import hyundai.partservice.app.inventory.adapter.dto.InventoryPartSupplierSectionDto;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.in.InventoryNewFilterUseCase;
import hyundai.partservice.app.inventory.application.port.out.InventoryNewFilterPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class InventoryNewFilterService implements InventoryNewFilterUseCase {

    private final InventoryNewFilterPort inventoryNewFilterPort;

    @Transactional
    @Override
    public InventoryFilterResponse filter(int page, int size, String searchType, String searchText, String orderType, String isDesc) {


        // 기본 정렬값 설정
        orderType = (orderType != null) ? orderType : "id";
        isDesc = (isDesc != null) ? isDesc : "true";

        // 정렬 설정
        Sort.Direction direction = isDesc.equalsIgnoreCase("true") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, mapOrderType(orderType));

        // 페이지 설정
        PageRequest pageRequest = PageRequest.of(Math.max(page - 1, 0), size, sort);

        // 검색 조건 적용
        Page<Inventory> resultPage = (searchType != null && searchText != null)
                ? searchInventory(searchType, searchText, pageRequest)
                : inventoryNewFilterPort.findAllInventory(pageRequest);



        // Entity → DTO 변환
        Page<InventoryPartSupplierSectionDto> inventoryPartSupplierSectionDtos = resultPage.map(InventoryPartSupplierSectionDto::from);

        return InventoryFilterResponse.from(inventoryPartSupplierSectionDtos);
    }

    // 검색 처리 후 정렬 및 페이징 변환
    private Page<Inventory> searchInventory(String searchType, String searchText, PageRequest pageRequest) {
        List<Inventory> inventories = switch (searchType) {
            case "부품 코드" -> getInventoriesByPartId(searchText);
            case "부품명" -> getInventoriesByPartName(searchText);
            case "적재 위치" -> getInventoriesBySectionName(searchText);
            case "납품 업체명" -> getInventoriesBySupplierName(searchText);
            default -> inventoryNewFilterPort.findAllInventory(pageRequest).getContent();
        };

        // 정렬 후 페이징 적용
        return convertListToPage(inventories, pageRequest);
    }

    // 부품 ID로 Inventory 리스트 가져오기
    private List<Inventory> getInventoriesByPartId(String partId) {
        return inventoryNewFilterPort.findByPartIdContaining(partId).stream()
                .flatMap(part -> part.getInventoryList().stream())
                .toList();
    }

    // 부품명으로 Inventory 리스트 가져오기
    private List<Inventory> getInventoriesByPartName(String partName) {
        return inventoryNewFilterPort.findByPartNameContaining(partName).stream()
                .flatMap(part -> part.getInventoryList().stream())
                .toList();
    }

    // 적재 위치(Section)으로 Inventory 리스트 가져오기
    private List<Inventory> getInventoriesBySectionName(String sectionName) {
        return inventoryNewFilterPort.findBySectionNameContaining(sectionName).stream()
                .flatMap(section -> section.getInventories().stream())
                .toList();
    }

    // 납품 업체명(Supplier)으로 Inventory 리스트 가져오기
    private List<Inventory> getInventoriesBySupplierName(String supplierName) {
        Set<Inventory> inventorySet = new HashSet<>();
        inventoryNewFilterPort.findBySupplierContaining(supplierName).getParts()
                .forEach(part -> inventorySet.addAll(part.getInventoryList()));
        return new ArrayList<>(inventorySet);
    }

    // List → Page 변환 및 정렬 적용
    private Page<Inventory> convertListToPage(List<Inventory> inventories, PageRequest pageRequest) {
        // 정렬 적용
        List<Inventory> sortedInventories = inventories.stream()
                .sorted(getInventoryComparator(pageRequest.getSort()))
                .toList();

        // 페이징 적용
        int start = (int) pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getPageSize(), sortedInventories.size());
        List<Inventory> pagedInventories = sortedInventories.subList(start, end);

        return new PageImpl<>(pagedInventories, pageRequest, sortedInventories.size());
    }

    // 정렬 기준을 Comparator로 변환
    private Comparator<Inventory> getInventoryComparator(Sort sort) {
        return sort.stream()
                .map(order -> {
                    Comparator<Inventory> comparator;
                    switch (order.getProperty()) {
                        case "partQuantity" -> comparator = Comparator.comparing(Inventory::getPartQuantity);
                        case "safetyStock" -> comparator = Comparator.comparing(inv -> inv.getPart().getSafetyStock());
                        default -> comparator = Comparator.comparing(Inventory::getId);
                    }
                    return order.isAscending() ? comparator : comparator.reversed();
                })
                .reduce(Comparator::thenComparing)
                .orElse(Comparator.comparing(Inventory::getId));
    }

    // 정렬 기준 매핑 (수정된 부분)
    private String mapOrderType(String orderType) {
        return switch (orderType) {
            case "현재 수량", "현재수량" -> "partQuantity";
            case "안전 수량", "안전수량" -> "part.safetyStock";  // 수정된 부분
            default -> "id";
        };
    }
}
