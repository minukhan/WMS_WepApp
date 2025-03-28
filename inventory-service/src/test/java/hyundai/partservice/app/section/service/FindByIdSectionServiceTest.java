package hyundai.partservice.app.section.service;


import hyundai.partservice.app.section.adapter.dto.SectionResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.FindByIdSectionPort;
import hyundai.partservice.app.section.application.service.FindByIdSectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class FindByIdSectionServiceTest {

    @Mock
    private FindByIdSectionPort findByIdSectionPort;

    @InjectMocks
    private FindByIdSectionService findByIdSectionService;

    private Section section;

    @BeforeEach
    void setUp() {
        section = Section.builder()
                .id(1L)
                .name("Engine Section")
                .maxCapacity(5000)
                .quantity(3550)
                .build();
    }

    @Test
    void findById_ShouldReturnSectionResponse_WhenSectionExists() {
        // Given
        Long sectionId = 1L;
        when(findByIdSectionPort.findSection(sectionId)).thenReturn(section);

        // When
        SectionResponse response = findByIdSectionService.findById(sectionId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.sectionDto().sectionId()).isEqualTo(sectionId);
        assertThat(response.sectionDto().sectionName()).isEqualTo("Engine Section");
        assertThat(response.sectionDto().quantity()).isEqualTo(3550);
    }

}
