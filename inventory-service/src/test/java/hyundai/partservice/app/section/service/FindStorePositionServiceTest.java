package hyundai.partservice.app.section.service;


import hyundai.partservice.app.section.adapter.dto.SectionFindPositionRequest;
import hyundai.partservice.app.section.adapter.dto.SectionFindPositionResponse;
import hyundai.partservice.app.section.adapter.dto.SectionQuantityDto;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.FindStorePositionPort;
import hyundai.partservice.app.section.application.service.FindStorePositionService;
import hyundai.partservice.app.section.exception.MaxWareHouseOverException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static hyundai.partservice.app.section.fake.FakeSectionFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindStorePositionServiceTest {

    @Mock
    private FindStorePositionPort findStorePositionPort;

    @InjectMocks
    private FindStorePositionService findStorePositionService;

    private List<Section> sections;

    @BeforeEach
    void setUp() {
        sections = Arrays.asList(
                createFakeSectionWithCapacity(), // 최대 10개 저장 가능, 현재 5개
                createFakeSectionWithCapacity2(), // 최대 15개 저장 가능, 현재 12개
                createFakeSectionWithCapacity3() // 최대 8개 저장 가능, 현재 3개
        );
    }

    @Test
    @DisplayName("✅ 충분한 공간이 있을 때 정상적으로 섹션을 할당해야 한다.")
    void shouldAllocateSectionsSuccessfully() {
        // given
        when(findStorePositionPort.findAllSections()).thenReturn(sections);

        SectionFindPositionRequest request = new SectionFindPositionRequest("PartId", 5);


        // when
        SectionFindPositionResponse response = findStorePositionService.findStorePosition(request);

        // then
        assertThat(response.SectionQuantityDtos().get(0).sectionId()).isEqualTo(3L);
        assertThat(response.SectionQuantityDtos()).hasSize(1);

        SectionQuantityDto firstSection = response.SectionQuantityDtos().get(0);
        assertThat(firstSection.allocatedAmount()).isEqualTo(5); // A3 먼저 할당


        verify(findStorePositionPort, times(1)).findAllSections();
    }

    @Test
    @DisplayName("🚨 공간이 부족하면 MaxWareHouseOverException 예외가 발생해야 한다.")
    void shouldThrowMaxWareHouseOverExceptionWhenNoSpaceAvailable() {
        // given
        when(findStorePositionPort.findAllSections()).thenReturn(sections);

        SectionFindPositionRequest request = new SectionFindPositionRequest("PartId2", 20000000); // 200000000개 필요 (공간 부족)

        // when & then
        assertThatThrownBy(() -> findStorePositionService.findStorePosition(request))
                .isInstanceOf(MaxWareHouseOverException.class);

        verify(findStorePositionPort, times(1)).findAllSections();
    }
}
