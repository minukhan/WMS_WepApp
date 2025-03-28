package hyundai.partservice.app.inventory.service;

import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.out.InventoryIncrementAndDecrementPartQuantityPort;
import hyundai.partservice.app.inventory.application.service.InventoryDecrementPartQuantityService;
import hyundai.partservice.app.inventory.exception.InventoryPartQuantityEmptyException;
import hyundai.partservice.app.part.fake.FakePartFactory;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.section.fake.FakeSectionFactory;
import hyundai.partservice.app.section.application.entity.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryDecrementPartQuantityServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private InventoryIncrementAndDecrementPartQuantityPort inventoryDecrementPartQuantityPort;

    @InjectMocks
    private InventoryDecrementPartQuantityService inventoryDecrementPartQuantityService;

    private Inventory inventory;
    private Part part;
    private Section section;

    @BeforeEach
    void setUp() {
        part = FakePartFactory.createFakePartWithoutSupplier();
        section = FakeSectionFactory.createFakeSectionWithCapacity();
        inventory = Inventory.builder()
                .id(1L)
                .partQuantity(5) // 초기 재고 5개
                .part(part)
                .section(section)
                .build();
    }

    @Test
    void 부품_재고_감소_성공() {
        // given
        when(inventoryDecrementPartQuantityPort.findInventoryBySectionAndPart(anyString(), anyInt(), anyString()))
                .thenReturn(inventory);

        ArgumentCaptor<Inventory> inventoryCaptor = ArgumentCaptor.forClass(Inventory.class);

        // when
        inventoryDecrementPartQuantityService.decrementPartQuantity("TEST123", "Fake Section A", 1);

        // then
        verify(inventoryDecrementPartQuantityPort, times(1)).saveInventory(inventoryCaptor.capture());

        Inventory savedInventory = inventoryCaptor.getValue(); // 실제 저장된 Inventory 객체 가져오기

        assertEquals(4, savedInventory.getPartQuantity()); // 저장된 객체의 partQuantity가
    }

    @Test
    void 부품_재고가_없으면_예외발생() {
        // given
        inventory = Inventory.builder()
                .id(1L)
                .partQuantity(0) // 재고가 0인 경우
                .part(part)
                .section(section)
                .build();

        when(inventoryDecrementPartQuantityPort.findInventoryBySectionAndPart("A", 2, "TEST123"))
                .thenReturn(inventory);

        // when & then
        assertThrows(InventoryPartQuantityEmptyException.class, () ->
                inventoryDecrementPartQuantityService.decrementPartQuantity("TEST123", "A", 2));

        verify(inventoryDecrementPartQuantityPort, never()).saveInventory(any(Inventory.class));
    }
}
