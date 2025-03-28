package hyundai.partservice.app.section.application.port.in;


import hyundai.partservice.app.section.adapter.dto.SectionListStorageResponse;
import hyundai.partservice.app.section.adapter.dto.SectionResponse;

public interface AllSectionToStorageUseCase {
    public abstract SectionListStorageResponse getAllSections();
}
