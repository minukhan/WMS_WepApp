package hyundai.partservice.app.section.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import hyundai.partservice.app.section.adapter.dto.SectionDto;
import hyundai.partservice.app.section.adapter.dto.SectionListResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.GetAllSectionPort;
import hyundai.partservice.app.section.application.service.GetAllSectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllSectionServiceTest {

    @InjectMocks
    private GetAllSectionService getAllSectionService;

    @Mock
    private GetAllSectionPort getAllSectionPort;

    private List<Section> mockSections;

    private Section section1;
    private Section section2;
    private Section section3;

    @BeforeEach
    void setUp() {
        section1 = Section.builder()
                .id(1L)
                .name("부품 엔진1")
                .maxCapacity(5000)
                .quantity(3550)
                .build();
        section2 = Section.builder()
                .id(1L)
                .name("부품 엔진2")
                .maxCapacity(5000)
                .quantity(3550)
                .build();
        section3 = Section.builder()
                .id(1L)
                .name("부품 엔진3")
                .maxCapacity(5000)
                .quantity(3550)
                .build();
        mockSections = Arrays.asList(section1,section2,section3);
    }

    @Test
    void allSections_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Section> sectionPage = new PageImpl<>(mockSections, pageable, mockSections.size());

        when(getAllSectionPort.getAllSections(pageable)).thenReturn(sectionPage);

        SectionListResponse response = getAllSectionService.allSections(pageable);


        assertThat(response).isNotNull();
        assertThat(response.content().get(0).sectionName()).isEqualTo("부품 엔진1");
        assertThat(response.content().get(1).sectionName()).isEqualTo("부품 엔진2");
        assertThat(response.content().get(2).sectionName()).isEqualTo("부품 엔진3");
    }
}