package hyundai.partservice.app.section.application.port.in;


import hyundai.partservice.app.section.adapter.dto.SectionPartInventoryResponse;

public interface FindByNameSectionPartInfoUseCase {
    public abstract SectionPartInventoryResponse findByNameSectionPartInfo(String name);
}
