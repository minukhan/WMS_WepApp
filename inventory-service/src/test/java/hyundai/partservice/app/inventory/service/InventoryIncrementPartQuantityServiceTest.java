package hyundai.partservice.app.inventory.service;

import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.out.InventoryIncrementAndDecrementPartQuantityPort;
import hyundai.partservice.app.inventory.application.service.InventoryIncrementPartQuantityService;
import hyundai.partservice.app.inventory.exception.InventoryOverCapacityException;
import hyundai.partservice.app.section.application.entity.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static hyundai.partservice.app.part.fake.FakePartFactory.createFakePartWithoutSupplier;
import static hyundai.partservice.app.section.fake.FakeSectionFactory.createFakeSectionWithCapacity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryIncrementPartQuantityServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private InventoryIncrementAndDecrementPartQuantityPort inventoryPort;

    @InjectMocks
    private InventoryIncrementPartQuantityService inventoryService;

    private Inventory inventory;
    private Section section;

    @BeforeEach
    void setUp() {
        section = createFakeSectionWithCapacity();

        inventory = Inventory.builder()
                .id(1L)
                .partQuantity(100) // 초기 수량
                .part(createFakePartWithoutSupplier())
                .section(section)
                .build();
        System.out.println(createFakeSectionWithCapacity());

        System.out.println("inventory: " + inventory);
        System.out.println("inventory Quantity: " + inventory.getSection().getQuantity()); // 여기가 null이면 문제 발생

    }

    @Test
    void shouldIncrementPartQuantityWhenCapacityIsNotExceeded() {
        // given
        when(inventoryPort.findInventoryBySectionAndPart("Section A", 1, "P123")).thenReturn(inventory);

        // when
        inventoryService.incrementPartQuantity("P123", 1, "Section A");

        // then
        verify(inventoryPort).findInventoryBySectionAndPart("Section A", 1, "P123");
        verify(inventoryPort).saveInventory(argThat(savedInventory ->
                savedInventory.getPartQuantity() == 101 // 기존 100에서 101로 증가
        ));
    }

    @Test
    void shouldThrowExceptionWhenPartQuantityExceedsCapacity() {
        // given
        inventory = Inventory.builder()
                .id(1L)
                .partQuantity(109) // 108 초과 상태
                .part(createFakePartWithoutSupplier())
                .section(createFakeSectionWithCapacity())
                .build();

        when(inventoryPort.findInventoryBySectionAndPart("Section A", 1, "P123")).thenReturn(inventory);

        // when & then
        assertThrows(InventoryOverCapacityException.class, () ->
                inventoryService.incrementPartQuantity("P123", 1, "Section A")
        );

        verify(inventoryPort, never()).saveInventory(any());
    }
}
