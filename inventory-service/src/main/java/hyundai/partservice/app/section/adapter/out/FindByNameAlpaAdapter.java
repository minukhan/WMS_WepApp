package hyundai.partservice.app.section.adapter.out;

import hyundai.partservice.app.infrastructure.repository.SectionRepository;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.FindByNameAlpaPort;
import hyundai.partservice.app.section.exception.SectionNameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindByNameAlpaAdapter implements FindByNameAlpaPort {

    private final SectionRepository sectionRepository;

    @Override
    public List<Section> findByName(String name) {

        List<Section> sections = sectionRepository.findByNameContaining(name);

        if(sections.isEmpty()){
            throw new SectionNameNotFoundException();
        }

        return sections;
    }
}
