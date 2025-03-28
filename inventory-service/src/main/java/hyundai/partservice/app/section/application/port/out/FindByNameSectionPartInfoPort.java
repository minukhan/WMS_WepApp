package hyundai.partservice.app.section.application.port.out;


import hyundai.partservice.app.section.application.entity.Section;

import java.util.List;

public interface FindByNameSectionPartInfoPort {
    public abstract List<Section> findByName(String name);
}
