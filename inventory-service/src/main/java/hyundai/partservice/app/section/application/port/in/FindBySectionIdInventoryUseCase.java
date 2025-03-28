package hyundai.partservice.app.section.application.port.in;

import hyundai.partservice.app.section.adapter.dto.SectionInventoryPartResponse;

public interface FindBySectionIdInventoryUseCase {
    public abstract SectionInventoryPartResponse findBySectionId(Long sectionId);
}
