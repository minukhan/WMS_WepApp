package hyundai.partservice.app.part.adapter.out;


import hyundai.partservice.app.infrastructure.repository.PartRepository;
import hyundai.partservice.app.part.application.port.out.GetAllPartPort;
import hyundai.partservice.app.part.application.entity.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GetAllPartAdapter implements GetAllPartPort {

    private final PartRepository partRepository;

    @Override
    public Page<Part> getAllParts(Pageable pageable) {
        return partRepository.findAll(pageable);
    }

}
