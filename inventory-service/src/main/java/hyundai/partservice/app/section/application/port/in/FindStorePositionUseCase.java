package hyundai.partservice.app.section.application.port.in;

import hyundai.partservice.app.section.adapter.dto.SectionFindPositionRequest;
import hyundai.partservice.app.section.adapter.dto.SectionFindPositionResponse;

public interface FindStorePositionUseCase {

    public abstract SectionFindPositionResponse findStorePosition(SectionFindPositionRequest request);
}
