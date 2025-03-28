package hyundai.partservice.app.section.adapter.out;


import hyundai.partservice.app.infrastructure.repository.SectionRepository;
import hyundai.partservice.app.inventory.exception.InventoryBySectionIdNotFoundException;
import hyundai.partservice.app.section.application.entity.Section;
import hyundai.partservice.app.section.application.port.out.FindBySectionIdInventoryPort;
import hyundai.partservice.app.section.exception.SectionIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class FindBySectionIdInventoryAdapter implements FindBySectionIdInventoryPort {

    private final SectionRepository sectionRepository;

    @Override
    public Section findBySectionId(Long sectionId) {
        Section section = sectionRepository.findByIdWithInventoryList(sectionId).orElse(null);

        return section;
    }

    @Override
    public Section findBySectionIds(Long sectionIds) {

        Section section = sectionRepository.findById(sectionIds).orElse(null);
        return section;
    }
}
