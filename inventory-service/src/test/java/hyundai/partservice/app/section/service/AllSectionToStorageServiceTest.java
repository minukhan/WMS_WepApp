package hyundai.partservice.app.section.service;


import hyundai.partservice.app.section.adapter.dto.SectionDto;
import hyundai.partservice.app.section.adapter.dto.SectionListStorageResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.AllSectionToStoragePort;
import hyundai.partservice.app.section.application.service.AllSectionToStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static hyundai.partservice.app.section.fake.FakeSectionFactory.createFakeSectionWithCapacity;
import static hyundai.partservice.app.section.fake.FakeSectionFactory.createFakeSectionWithCapacity2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AllSectionToStorageServiceTest {

    @Mock
    private AllSectionToStoragePort allSectionToStoragePort;

    @InjectMocks
    private AllSectionToStorageService allSectionToStorageService;

    @Test
    public void testGetAllSections() {
        // Given
        Section section1 = createFakeSectionWithCapacity();
        Section section2 = createFakeSectionWithCapacity2();

        when(allSectionToStoragePort.getAllSections()).thenReturn(List.of(section1, section2));

        // When
        SectionListStorageResponse response = allSectionToStorageService.getAllSections();

        // Then
        assertThat(response.sectionDtos()).hasSize(2);

        SectionDto firstSection = response.sectionDtos().get(0);
        assertThat(firstSection.sectionId()).isEqualTo(1L);
        assertThat(firstSection.sectionName()).isEqualTo("Fake Section A");

        SectionDto secondSection = response.sectionDtos().get(1);
        assertThat(secondSection.sectionId()).isEqualTo(2L);
        assertThat(secondSection.sectionName()).isEqualTo("Fake Section B");

        // Verify
        verify(allSectionToStoragePort, times(1)).getAllSections();
    }
}
