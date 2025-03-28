package hyundai.partservice.app.section.application.port.out;

import hyundai.partservice.app.section.application.entity.Section;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface GetAllSectionPort {
    Page<Section> getAllSections(Pageable pageable);
}
