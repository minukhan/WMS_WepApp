package hyundai.partservice.app.section.application.port.in;


import hyundai.partservice.app.section.adapter.dto.SectionListResponse;
import org.springframework.data.domain.Pageable;


public interface GetAllSectionUseCase {
    SectionListResponse allSections(Pageable pageable);
}
