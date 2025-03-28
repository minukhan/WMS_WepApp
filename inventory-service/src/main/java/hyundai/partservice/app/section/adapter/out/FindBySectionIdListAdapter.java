package hyundai.partservice.app.section.adapter.out;


import hyundai.partservice.app.infrastructure.repository.SectionRepository;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.FindBySectionIdListPort;
import hyundai.partservice.app.section.exception.SectionIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindBySectionIdListAdapter implements FindBySectionIdListPort {

    private final SectionRepository sectionRepository;

    @Override
    public List<Section> findBySectionIdList(List<Long> sectionIdList) {

        List<Section> sections = sectionRepository.findAllById(sectionIdList);

        if(sections.isEmpty()){
            throw new SectionIdNotFoundException();
        }

        return sections;
    }
}
