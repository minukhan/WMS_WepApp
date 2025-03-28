package hyundai.partservice.app.section.application.port.in;


import hyundai.partservice.app.section.adapter.dto.SectionPurchaseResponse;
import hyundai.partservice.app.section.adapter.dto.SectionResponse;
import hyundai.partservice.app.section.application.entity.Section;

import java.util.List;

public interface FindBySectionIdListUseCase {
    public abstract SectionPurchaseResponse findBySectionIdList(List<Long> sectionIds);
}
