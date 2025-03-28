package hyundai.partservice.app.part.service;



import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.section.adapter.dto.SectionPartInventoryResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.FindByNameSectionPartInfoPort;
import hyundai.partservice.app.section.application.service.FindByNameSectionPartInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static hyundai.partservice.app.inventory.fake.FakeInventoryFactory.createFakeInventory;
import static hyundai.partservice.app.inventory.fake.FakeInventoryFactory.createFakeInventory2;
import static hyundai.partservice.app.part.fake.FakePartFactory.createFakePartWithoutSupplier;
import static hyundai.partservice.app.part.fake.FakePartFactory.createFakePartWithoutSupplier2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Mockito 확장 활성화
class FindByNameSectionPartInfoServiceTest {

    @Mock
    private FindByNameSectionPartInfoPort findByNameSectionPartInfoPort;

    @InjectMocks
    private FindByNameSectionPartInfoService findByNameSectionPartInfoService;

    private Section section1;
    private Section section2;

    @BeforeEach
    void setUp() {
        // Mock 데이터 생성
        Part part1 = createFakePartWithoutSupplier();
        Part part2 = createFakePartWithoutSupplier2();

        Inventory inventory1 = createFakeInventory();
        Inventory inventory2 = createFakeInventory2();

        section1 = Section.builder()
                .id(1L)
                .name("창고 A")
                .inventories(List.of(inventory1))
                .floor(1)
                .build();

        section2 = Section.builder()
                .id(2L)
                .name("창고 B")
                .inventories(List.of(inventory2))
                .floor(2)
                .build();
    }

    @Test
    void testFindByNameSectionPartInfo() {
        // Given
        String sectionName = "창고 A";
        when(findByNameSectionPartInfoPort.findByName(sectionName))
                .thenReturn(List.of(section1, section2));

        // When
        SectionPartInventoryResponse response = findByNameSectionPartInfoService.findByNameSectionPartInfo(sectionName);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.sectionPartInventoryDtos()).hasSize(2);
        assertThat(response.sectionPartInventoryDtos().get(0).floor()).isEqualTo(1);
        assertThat(response.sectionPartInventoryDtos().get(0).partName()).isEqualTo("Fake Part Name");
        assertThat(response.sectionPartInventoryDtos().get(0).partQuantity()).isEqualTo(50);

        assertThat(response.sectionPartInventoryDtos().get(1).floor()).isEqualTo(2);
        assertThat(response.sectionPartInventoryDtos().get(1).partName()).isEqualTo("Fake Part Name B");
        assertThat(response.sectionPartInventoryDtos().get(1).partQuantity()).isEqualTo(60);

        // Verify (Mock 메서드가 1번만 호출되었는지 확인)
        verify(findByNameSectionPartInfoPort, times(1)).findByName(sectionName);
    }
}