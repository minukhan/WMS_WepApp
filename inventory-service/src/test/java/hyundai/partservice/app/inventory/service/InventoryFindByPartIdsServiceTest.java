package hyundai.partservice.app.inventory.service;

import hyundai.partservice.app.inventory.adapter.dto.InventorySectionDto;
import hyundai.partservice.app.inventory.adapter.dto.InventoryStorageResponse;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.out.InventoryFindByPartIdsPort;
import hyundai.partservice.app.inventory.application.service.InventoryFindByPartIdsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static hyundai.partservice.app.inventory.fake.FakeInventoryFactory.createFakeInventory;
import static hyundai.partservice.app.inventory.fake.FakeInventoryFactory.createFakeInventory2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryFindByPartIdsServiceTest {

    @Mock
    private InventoryFindByPartIdsPort inventoryFindByPartIdsPort;

    @InjectMocks
    private InventoryFindByPartIdsService inventoryFindByPartIdsService;

    private List<Inventory> mockInventories;

    @BeforeEach
    void setUp() {
        // 테스트용 Inventory 객체 생성
        Inventory inventory1 = createFakeInventory();
        Inventory inventory2 = createFakeInventory2();

        mockInventories = List.of(inventory1, inventory2);
    }

    @Test
    void testFindByPartIds_Success() {
        // Given
        List<String> partIds = List.of("123123", "234234");
        when(inventoryFindByPartIdsPort.findByPartIds(partIds)).thenReturn(mockInventories);

        // When
        InventoryStorageResponse response = inventoryFindByPartIdsService.findByPartIds(partIds);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.inventorySectionDtos()).hasSize(2);

        InventorySectionDto firstInventory = response.inventorySectionDtos().get(0);
        assertThat(firstInventory.partId()).isEqualTo("123123");
        assertThat(firstInventory.partQuantity()).isEqualTo(50);

        verify(inventoryFindByPartIdsPort, times(1)).findByPartIds(partIds);
    }

    @Test
    void testFindByPartIds_EmptyResult() {
        // Given
        List<String> partIds = List.of("part-003"); // 존재하지 않는 부품 ID
        when(inventoryFindByPartIdsPort.findByPartIds(partIds)).thenReturn(List.of());

        // When
        InventoryStorageResponse response = inventoryFindByPartIdsService.findByPartIds(partIds);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.inventorySectionDtos()).isEmpty();

        verify(inventoryFindByPartIdsPort, times(1)).findByPartIds(partIds);
    }
}
