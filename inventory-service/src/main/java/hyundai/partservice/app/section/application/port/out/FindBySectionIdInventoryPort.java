package hyundai.partservice.app.section.application.port.out;

import hyundai.partservice.app.section.application.entity.Section;


public interface FindBySectionIdInventoryPort {

    public abstract Section findBySectionId(Long sectionId);
    public abstract Section findBySectionIds(Long sectionIds);
}
