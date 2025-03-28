package hyundai.partservice.app.section.service;


import hyundai.partservice.app.section.adapter.dto.SectionFindNameResponse;
import hyundai.partservice.app.section.adapter.dto.SectionResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.FindByNameSectionPort;
import hyundai.partservice.app.section.application.service.FindByNameSectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class FindByNameSectionServiceTest {

    @Mock
    private FindByNameSectionPort findByNameSectionPort;  // ✅ Mock 객체 설정

    @InjectMocks
    private FindByNameSectionService findByNameSectionService; // ✅ Mock 객체 주입

    private Section section;

    private List<Section> sections;

    @BeforeEach
    void setUp() {
        section = Section.builder()
                .id(1L)
                .name("부품 엔진")
                .maxCapacity(5000)
                .quantity(3550)
                .build();

        sections = List.of(section);
    }

    @Test
    void getfindByNameSection() {
        // given
        String name = "부품 엔진";
        when(findByNameSectionPort.findByName(name)).thenReturn(sections);  // ✅ Mock 객체가 올바르게 호출되도록 수정

        // when
        SectionFindNameResponse response = findByNameSectionService.findByName(name);

        // then
        assertThat(response).isNotNull();
        assertThat(response.sectionDtos().get(0).sectionName()).isEqualTo(name);
    }
}