package hyundai.partservice.app.section.application.service;

import hyundai.partservice.app.section.adapter.dto.SectionDto;
import hyundai.partservice.app.section.adapter.dto.SectionListStorageResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.in.AllSectionToStorageUseCase;
import hyundai.partservice.app.section.application.port.out.AllSectionToStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AllSectionToStorageService implements AllSectionToStorageUseCase {

    private final AllSectionToStoragePort allSectionToStoragePort;

    @Override
    public SectionListStorageResponse getAllSections() {

        List<Section> sections = allSectionToStoragePort.getAllSections();

        List<SectionDto> sectionDtos = sections.stream().map(section -> SectionDto.from(section))
                .collect(Collectors.toList());

        return SectionListStorageResponse.from(sectionDtos);
    }
}
