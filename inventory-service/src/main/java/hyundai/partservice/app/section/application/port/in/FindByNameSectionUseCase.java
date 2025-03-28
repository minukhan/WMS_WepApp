package hyundai.partservice.app.section.application.port.in;


import hyundai.partservice.app.section.adapter.dto.SectionFindNameResponse;

public interface FindByNameSectionUseCase {

    public abstract SectionFindNameResponse findByName(String name);
}
