package hyundai.partservice.app.inventory.adapter.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record InventoryFilterResponse(
        List<InventoryPartSupplierSectionDto> inventoryPartSupplierSectionDtos,
        int currentPage,
        int totalPages,
        long totalElements,
        boolean isFirst,
        boolean isLast
) {
    public static InventoryFilterResponse from(Page<InventoryPartSupplierSectionDto> page) {
        return new InventoryFilterResponse(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast()
        );
    }
}
