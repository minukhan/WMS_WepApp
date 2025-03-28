package hyundai.partservice.app.part.application.port.out;


import hyundai.partservice.app.part.application.entity.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GetAllPartPort {
    Page<Part> getAllParts(Pageable pageable);
}
