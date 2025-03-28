package hyundai.partservice.app.section.adapter.dto;

import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.section.application.entity.Section;

public record SectionPartInventoryDto(
        int floor,
        String partName,
        int partQuantity
) {
    public static SectionPartInventoryDto of(int floor, String partName, int partQuantity) {
        return new SectionPartInventoryDto(
                floor,
                partName,
                partQuantity
        );
    }
}
