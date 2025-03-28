package hyundai.partservice.app.section.adapter.out;

import hyundai.partservice.app.infrastructure.repository.SectionRepository;
import hyundai.partservice.app.section.application.port.out.EmptySpacePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmptySpaceAdapter implements EmptySpacePort {

    private final SectionRepository sectionRepository;

    @Override
    public int getTotalQuantity() {
        return sectionRepository.findTotalMaxCapacity();
    }

    @Override
    public int currentQuantity() {
        return sectionRepository.findTotalCurrentQuantity();
    }
}
