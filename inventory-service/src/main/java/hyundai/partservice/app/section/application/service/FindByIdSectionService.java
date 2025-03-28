package hyundai.partservice.app.section.application.service;
import hyundai.partservice.app.section.adapter.dto.SectionDto;
import hyundai.partservice.app.section.adapter.dto.SectionResponse;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.in.FindByIdSectionUseCase;
import hyundai.partservice.app.section.application.port.out.FindByIdSectionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindByIdSectionService implements FindByIdSectionUseCase {

    private final FindByIdSectionPort findSectionPort;

    @Override
    public SectionResponse findById(Long id) {
        Section section = findSectionPort.findSection(id);
        SectionDto sectionDto = SectionDto.from(section);
        return SectionResponse.from(sectionDto);
    }
}
