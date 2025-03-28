package hyundai.partservice.app.inventory.application.service;

import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.in.InventoryIncrementPartQuantityUseCase;
import hyundai.partservice.app.inventory.application.port.out.InventoryIncrementAndDecrementPartQuantityPort;
import hyundai.partservice.app.inventory.exception.InventoryOverCapacityException;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.section.application.entity.Section;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryIncrementPartQuantityService implements InventoryIncrementPartQuantityUseCase {

    // ✅ 헥사고날 아키텍처 원칙에 따라 동일한 Port 인터페이스 사용
    private final InventoryIncrementAndDecrementPartQuantityPort inventoryDecrementPartQuantityPort;

    @Override
    public void incrementPartQuantity(String partId, int floor, String sectionName) {
        Inventory inventory = inventoryDecrementPartQuantityPort.findInventoryBySectionAndPart(sectionName, floor, partId);
        Part associatedPart = inventoryDecrementPartQuantityPort.findByPartId(partId);
        Section associatedSection = inventoryDecrementPartQuantityPort.findBySectionNameAndFloor(sectionName, floor);

        // inventory가 없다면 새로 생성
        if (inventory == null) {
            inventory = Inventory.builder()
                    .partQuantity(1) // 기본 수량 1로 설정
                    .part(associatedPart)
                    .section(associatedSection)
                    .build();

            // 새로 생성된 inventory 저장
            inventoryDecrementPartQuantityPort.saveInventory(inventory);
        } else {
            // 수량이 108을 초과하지 않도록 체크
            if (inventory.getPartQuantity() >= 108) {
                throw new InventoryOverCapacityException();  // 수량 초과 시 예외
            }
            // inventory 수량 증가
            inventory.updatePartQuantity(inventory.getPartQuantity() + 1);
        }

        // ✅ Part의 전체 수량도 증가
        associatedPart.updateQuantity(associatedPart.getQuantity() + 1);

        // ✅ Section의 수량도 증가
        Section section = inventoryDecrementPartQuantityPort.findBySectionId(inventory.getSection().getId());
        section.updateQuantity(section.getQuantity() + 1);

        // ✅ 업데이트된 데이터 저장
        inventoryDecrementPartQuantityPort.saveInventory(inventory);  // Inventory 업데이트
        inventoryDecrementPartQuantityPort.savePart(associatedPart);  // Part 업데이트
        inventoryDecrementPartQuantityPort.saveSection(section);  // Section 업데이트
    }
}
