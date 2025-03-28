package hyundai.partservice.app.section.application.service;

import hyundai.partservice.app.section.adapter.dto.SectionDto;
import hyundai.partservice.app.section.adapter.dto.SectionFindNameResponse;
import hyundai.partservice.app.section.adapter.dto.SectionResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.in.FindByNameSectionUseCase;
import hyundai.partservice.app.section.application.port.out.FindByNameSectionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindByNameSectionService implements FindByNameSectionUseCase {

    private final FindByNameSectionPort findByNameSectionPort;

    @Override
    public SectionFindNameResponse findByName(String sectionName) {

        List<Section> sections = findByNameSectionPort.findByName(sectionName);
        //Dto 반환
        List<SectionDto> sectionDtos = sections.stream()
                .map((section)-> SectionDto.from(section))
                .collect(Collectors.toList());

        return SectionFindNameResponse.from(sectionDtos);
    }

}
