package hyundai.partservice.app.part.application.port.in;

import hyundai.partservice.app.part.adapter.dto.PartListPaginationResponse;
import org.springframework.data.domain.Pageable;


public interface GetAllPartUseCase {
    PartListPaginationResponse getAllParts(Pageable pageable );
}
