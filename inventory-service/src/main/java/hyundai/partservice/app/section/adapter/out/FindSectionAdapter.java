package hyundai.partservice.app.section.adapter.out;

import hyundai.partservice.app.infrastructure.repository.SectionRepository;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.FindByIdSectionPort;
import hyundai.partservice.app.section.exception.SectionIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindSectionAdapter implements FindByIdSectionPort {

    private final SectionRepository sectionRepository;

    @Override
    public Section findSection(Long id){
        Section section = sectionRepository.findById(id).orElseThrow(() -> new SectionIdNotFoundException());
        return section;
    }
}
