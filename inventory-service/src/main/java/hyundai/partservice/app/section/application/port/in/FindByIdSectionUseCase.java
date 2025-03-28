package hyundai.partservice.app.section.application.port.in;


import hyundai.partservice.app.section.adapter.dto.SectionResponse;

public interface FindByIdSectionUseCase {
    SectionResponse findById(Long id);
}
