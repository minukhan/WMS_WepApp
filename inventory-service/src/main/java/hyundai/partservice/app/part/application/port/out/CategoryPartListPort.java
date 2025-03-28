package hyundai.partservice.app.part.application.port.out;

import hyundai.partservice.app.part.application.entity.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryPartListPort  {
    public abstract Page<Part> getCategoryPartList(String category, Pageable pageable);
}
