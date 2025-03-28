package hyundai.partservice.app.section.adapter.out;

import hyundai.partservice.app.infrastructure.repository.SectionRepository;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.GetAllSectionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AllSectionAdapter implements GetAllSectionPort {
    private final SectionRepository sectionRepository;

    @Override
    public Page<Section> getAllSections(Pageable pageable) {
        return sectionRepository.findAll(pageable);
    }
}
