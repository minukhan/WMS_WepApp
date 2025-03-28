package hyundai.partservice.app.section.service;



import hyundai.partservice.app.inventory.adapter.dto.InventoryDto;
import hyundai.partservice.app.inventory.adapter.dto.InventoryPartDto;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.section.adapter.dto.SectionDto;
import hyundai.partservice.app.section.adapter.dto.SectionInventoryPartResponse;
import hyundai.partservice.app.section.adapter.dto.SectionInventoryResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.FindBySectionIdInventoryPort;
import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.section.application.service.FindBySectionIdInventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static hyundai.partservice.app.part.fake.FakePartFactory.*;
import static hyundai.partservice.app.section.fake.FakeSectionFactory.createFakeSectionWithInventories;
import static hyundai.partservice.app.section.fake.FakeSectionFactory.createFakeSectionWithInventoriesWithParts;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindBySectionIdInventoryServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private FindBySectionIdInventoryPort findBySectionIdInventoryPort;

    @InjectMocks
    private FindBySectionIdInventoryService findBySectionIdInventoryService;

    private Section section;
    private Section section2;
    private Inventory inventory1;
    private Inventory inventory2;
    private Part part;
    private Part part2;

    @BeforeEach
    void setUp() {

        section2 =  Section.builder()
                .id(2L)
                .name("Fake Section B")
                .floor(2)
                .maxCapacity(200)
                .build();
        part = createFakePartWithSupplier();

        // Section 및 Inventory 객체 생성
        section = createFakeSectionWithInventoriesWithParts(section2, part);

    }

    @Test
    @DisplayName("섹션 ID로 재고 정보를 조회하면 올바른 DTO 응답을 반환해야 한다")
    void findBySectionId_ShouldReturnSectionInventoryResponse() {
        // given
        when(findBySectionIdInventoryPort.findBySectionId(2L)).thenReturn(section);

        // when
        SectionInventoryPartResponse response = findBySectionIdInventoryService.findBySectionId(2L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.sectionDto()).isEqualTo(SectionDto.from(section));
        assertThat(response.inventories()).hasSize(1);

        InventoryPartDto dto1 = response.inventories().get(0);

        assertThat(dto1.inventoryId()).isEqualTo(1L);
        assertThat(dto1.partQuantity()).isEqualTo(10);
        assertThat(dto1.partId()).isEqualTo("TEST456");

        // verify
        verify(findBySectionIdInventoryPort, times(1)).findBySectionId(2L);
    }
}
