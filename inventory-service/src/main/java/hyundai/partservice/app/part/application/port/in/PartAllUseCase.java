package hyundai.partservice.app.part.application.port.in;

import hyundai.partservice.app.part.adapter.dto.PartListResponse;

public interface PartAllUseCase {

    public abstract PartListResponse getAllParts();
}
