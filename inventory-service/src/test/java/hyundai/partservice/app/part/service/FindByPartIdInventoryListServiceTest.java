package hyundai.partservice.app.part.service;

import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.part.adapter.dto.PartInventoryResponse;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.FIndByPartIdInventoryListPort;
import hyundai.partservice.app.part.application.service.FindByPartIdInventoryListService;
import hyundai.partservice.app.section.application.entity.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FIndByPartIdInventoryListServiceTest {

    @Mock
    private FIndByPartIdInventoryListPort findByPartIdInventoryListPort;

    @InjectMocks
    private FindByPartIdInventoryListService findByPartIdInventoryListService;

    private Part part;
    private Inventory inventory;
    private Section section;

    @BeforeEach
    void setUp() {
        // 가짜 Section 생성
        section = Section.builder()
                .id(10L)
                .name("Storage A")
                .quantity(55)
                .maxCapacity(105)
                .build();

        // 가짜 Inventory 엔티티 먼저 생성 (part 없이!)
        inventory = Inventory.builder()
                .id(1L)
                .partQuantity(50)
                .section(section)  // ✅ Section만 먼저 설정
                .part(new Part())
                .build();

        // 가짜 Part 엔티티 생성
        part = Part.builder()
                .id("P123")
                .name("Brake Pad")
                .quantity(100)
                .safetyStock(10)
                .maxStock(200)
                .optimalStock(150)
                .deliveryDuration(5)
                .price(5000L)
                .category("Brakes")
                .inventoryList(new ArrayList<>())  // ✅ 빈 리스트로 초기화
                .build();


        // Part에 Inventory 추가
        part.getInventoryList().add(inventory);
    }

    @Test
    void getPartInventory_성공() {
        // given
        String partId = "P123";
        when(findByPartIdInventoryListPort.findPartByPartIdInventory(partId)).thenReturn(part);

        // when
        PartInventoryResponse response = findByPartIdInventoryListService.getPartInventory(partId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.partDto()).isNotNull();
        assertThat(response.partDto().partId()).isEqualTo(part.getId());
        assertThat(response.partDto().partName()).isEqualTo(part.getName());

        assertThat(response.inventoryDtoList()).isNotEmpty();
        assertThat(response.inventoryDtoList().get(0).partQuantity()).isEqualTo(50);

        // findByPartIdInventoryListPort.findPartByPartIdInventory()가 1번 호출되었는지 검증
        verify(findByPartIdInventoryListPort, times(1)).findPartByPartIdInventory(partId);
    }
}