package hyundai.partservice.app.section.application.service;


import hyundai.partservice.app.section.adapter.dto.SectionDto;
import hyundai.partservice.app.section.adapter.dto.SectionListResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.in.GetAllSectionUseCase;
import hyundai.partservice.app.section.application.port.out.GetAllSectionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GetAllSectionService implements GetAllSectionUseCase {

    private final GetAllSectionPort getAllSectionPort;

    @Override
    public SectionListResponse allSections(Pageable pageable){
        Page<Section> sections = getAllSectionPort.getAllSections(pageable);
        Page<SectionDto> sectionDtos = sections.map((section -> SectionDto.from(section)));
        return SectionListResponse.from(sectionDtos);
    }
}
