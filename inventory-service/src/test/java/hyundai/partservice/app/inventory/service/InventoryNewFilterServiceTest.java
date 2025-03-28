package hyundai.partservice.app.inventory.service;

import hyundai.partservice.app.inventory.adapter.dto.InventoryFilterResponse;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.out.InventoryNewFilterPort;
import hyundai.partservice.app.inventory.application.service.InventoryNewFilterService;
import hyundai.partservice.app.part.application.entity.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

import static hyundai.partservice.app.inventory.fake.FakeInventoryFactory.createFakeInventory;
import static hyundai.partservice.app.part.fake.FakePartFactory.createFakePartWithSupplier;
import static hyundai.partservice.app.part.fake.FakePartFactory.createFakePartWithoutSupplier;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryNewFilterServiceTest {

    @InjectMocks
    private InventoryNewFilterService inventoryNewFilterService;

    @Mock
    private InventoryNewFilterPort inventoryNewFilterPort;

    private Inventory inventory;
    private Part part;

    @BeforeEach
    void setUp() {
        part = createFakePartWithSupplier();
        inventory = createFakeInventory();
    }

//    @Test
//    void testFilterWithoutSearch() {
//        // Given
//        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
//        Page<Inventory> mockPage = new PageImpl<>(List.of(inventory), pageRequest, 1);
//
//        when(inventoryNewFilterPort.findAllInventory(pageRequest)).thenReturn(mockPage);
//
//        // When
//        InventoryFilterResponse response = inventoryNewFilterService.filter(1, 10, null, null, "id", "true");
//
//        // Then
//        assertThat(response).isNotNull();
//        assertThat(response.inventoryPartSupplierSectionDtos()).hasSize(1);
//        verify(inventoryNewFilterPort, times(1)).findAllInventory(pageRequest);
//    }
//
//    @Test
//    void testFilterWithSearchByPartId() {
//        // Given
//        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "partQuantity"));
//        when(inventoryNewFilterPort.findByPartId("ABC123")).thenReturn(part);
//
//        // When
//        InventoryFilterResponse response = inventoryNewFilterService.filter(1, 10, "부품 코드", "ABC123", "현재 수량", "false");
//
//        // Then
//        assertThat(response).isNotNull();
//        verify(inventoryNewFilterPort, times(1)).findByPartId("ABC123");
//    }

    @Test
    void testFilterWithEmptyResult() {
        // Given
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Inventory> emptyPage = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        when(inventoryNewFilterPort.findAllInventory(pageRequest)).thenReturn(emptyPage);

        // When
        InventoryFilterResponse response = inventoryNewFilterService.filter(1, 10, null, null, "id", "true");

        // Then
        assertThat(response).isNotNull();
        assertThat(response.inventoryPartSupplierSectionDtos()).isEmpty();
        verify(inventoryNewFilterPort, times(1)).findAllInventory(pageRequest);
    }
}

