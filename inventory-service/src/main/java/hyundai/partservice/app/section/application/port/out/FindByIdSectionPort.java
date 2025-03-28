package hyundai.partservice.app.section.application.port.out;

import hyundai.partservice.app.section.application.entity.Section;

public interface FindByIdSectionPort {
    Section findSection(Long id);
}
