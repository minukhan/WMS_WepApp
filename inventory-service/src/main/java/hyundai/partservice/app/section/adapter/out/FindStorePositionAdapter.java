package hyundai.partservice.app.section.adapter.out;


import hyundai.partservice.app.infrastructure.repository.SectionRepository;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.FindStorePositionPort;
import hyundai.partservice.app.section.exception.SectionIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindStorePositionAdapter implements FindStorePositionPort {

    private final SectionRepository sectionRepository;


    @Override
    public List<Section> findAllSections() {

        List<Section> sections  = sectionRepository.findAll();

        if(sections.isEmpty()){
            throw new SectionIdNotFoundException();
        }

        return sections;
    }
}
