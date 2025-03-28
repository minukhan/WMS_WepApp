package hyundai.partservice.app.part.application.port.in;


import hyundai.partservice.app.part.adapter.dto.PartListPaginationResponse;
import org.springframework.data.domain.Pageable;

public interface CategoryPartListUseCase {
    public abstract PartListPaginationResponse getPartList(String category, Pageable pageable);
}
