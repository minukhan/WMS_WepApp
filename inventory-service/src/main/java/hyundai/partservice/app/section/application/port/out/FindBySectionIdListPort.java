package hyundai.partservice.app.section.application.port.out;


import hyundai.partservice.app.section.application.entity.Section;

import java.util.List;

public interface FindBySectionIdListPort {
    public abstract List<Section> findBySectionIdList(List<Long> sectionIdList);
}
