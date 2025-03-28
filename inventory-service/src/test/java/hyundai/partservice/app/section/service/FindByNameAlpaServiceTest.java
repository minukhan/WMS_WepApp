package hyundai.partservice.app.section.service;

import hyundai.partservice.app.inventory.adapter.dto.InventoryDto;
import hyundai.partservice.app.inventory.adapter.dto.InventoryListResponse;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.fake.FakeInventoryFactory;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.fake.FakePartFactory;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.FindByNameAlpaPort;
import hyundai.partservice.app.section.application.service.FindByNameAlpaService;
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
public class FindByNameAlpaServiceTest {

    @Mock
    private FindByNameAlpaPort findByNameAlpaPort;

    @InjectMocks
    private FindByNameAlpaService findByNameAlpaService;

    @Test
    public void testFindByAlpa() {
        // Given
        Part fakePart = FakePartFactory.createFakePartWithoutSupplier();

        // Section 생성 (빈 리스트로 초기화)
        Section fakeSectionA = Section.builder()
                .id(1L)
                .name("Fake Section A")
                .floor(1)
                .quantity(10)
                .maxCapacity(100)
                .inventories(new ArrayList<>())  // 빈 리스트로 초기화
                .build();

        // Inventory 생성 및 연결
        Inventory fakeInventory = Inventory.builder()
                .id(1L)
                .part(fakePart)
                .section(fakeSectionA)
                .partQuantity(10)
                .build();

        // Section에 Inventory 추가
        fakeSectionA.getInventories().add(fakeInventory);

        // Mock 설정
        when(findByNameAlpaPort.findByName("Fake Section A")).thenReturn(List.of(fakeSectionA));

        // When
        InventoryListResponse response = findByNameAlpaService.findByAlpa("Fake Section A");

        // Then
        assertThat(response.inventoryDtos()).hasSize(1);

        InventoryDto inventoryDto = response.inventoryDtos().get(0);
        assertThat(inventoryDto.partId()).isEqualTo(fakePart.getId());
        assertThat(inventoryDto.sectionId()).isEqualTo(fakeSectionA.getId());
        assertThat(inventoryDto.partQuantity()).isEqualTo(fakeInventory.getPartQuantity());

        // Verify
        verify(findByNameAlpaPort, times(1)).findByName("Fake Section A");
    }
}
