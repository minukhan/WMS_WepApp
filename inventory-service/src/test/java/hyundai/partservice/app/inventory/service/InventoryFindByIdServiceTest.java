package hyundai.partservice.app.inventory.service;
import hyundai.partservice.app.inventory.adapter.dto.InventoryResponse;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.application.port.out.InventoryFindByIdPort;
import hyundai.partservice.app.inventory.application.service.InventoryFindByIdService;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.section.application.entity.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryFindByIdServiceTest {

    @Mock
    private InventoryFindByIdPort inventoryFindByIdPort;

    @InjectMocks
    private InventoryFindByIdService inventoryFindByIdService;

    private Inventory mockInventory;

    @BeforeEach
    void setUp() {

        // 가짜 Section 객체 생성
        Section section = Section.builder()
                .id(100L)
                .name("Engine Section")
                .build();

        Part part = Part.builder()
                .id("123123123")
                .name("Engine Section")
                .build();

        // 가짜 Inventory 객체 생성
        mockInventory = Inventory.builder()
                .id(1L)
                .section(section)
                .part(part)
                .partQuantity(10)
                .build();
    }

    @Test
    void findById_ReturnsInventoryResponse() {
        // given
        when(inventoryFindByIdPort.findById(1L)).thenReturn(mockInventory);

        // when
        InventoryResponse response = inventoryFindByIdService.findById(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.inventoryDto()).isNotNull();
        assertThat(response.inventoryDto().inventoryId()).isEqualTo(mockInventory.getId());
        assertThat(response.inventoryDto().partQuantity()).isEqualTo(mockInventory.getPartQuantity());

        // findById가 정확히 한 번 호출되었는지 검증
        verify(inventoryFindByIdPort, times(1)).findById(1L);
    }
}
