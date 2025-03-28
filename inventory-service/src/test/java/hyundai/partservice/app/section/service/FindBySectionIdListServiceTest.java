package hyundai.partservice.app.section.service;


import hyundai.partservice.app.section.adapter.dto.SectionDto;
import hyundai.partservice.app.section.adapter.dto.SectionPurchaseResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.FindBySectionIdListPort;
import hyundai.partservice.app.section.application.service.FindBySectionIdListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static hyundai.partservice.app.section.fake.FakeSectionFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindBySectionIdListServiceTest {

    @Mock
    private FindBySectionIdListPort findBySectionIdListPort;

    @InjectMocks
    private FindBySectionIdListService findBySectionIdListService;

    private List<Section> mockSections;
    private List<Long> sectionIds;

    @BeforeEach
    void setUp() {
        // 테스트용 Section 객체 생성
        Section section1 = createFakeSectionWithCapacity();
        Section section2 = createFakeSectionWithCapacity2();
        mockSections = List.of(section1, section2);

        // 테스트할 Section ID 리스트
        sectionIds = List.of(101L, 102L);
    }

    @Test
    void findBySectionIdList_ShouldReturnSectionPurchaseResponse() {
        // Given: Mock 설정
        when(findBySectionIdListPort.findBySectionIdList(sectionIds)).thenReturn(mockSections);

        // When: 서비스 메서드 호출
        SectionPurchaseResponse response = findBySectionIdListService.findBySectionIdList(sectionIds);

        // Then: 검증
        assertThat(response).isNotNull();
        assertThat(response.sectionDtos()).hasSize(2);
        assertThat(response.sectionDtos()).extracting(SectionDto::sectionId).containsExactly(1L, 2L);

        // Mock 메서드가 1번 호출되었는지 검증
        verify(findBySectionIdListPort, times(1)).findBySectionIdList(sectionIds);
    }
}
