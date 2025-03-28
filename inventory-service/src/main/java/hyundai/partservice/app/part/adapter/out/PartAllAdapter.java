package hyundai.partservice.app.part.adapter.out;

import hyundai.partservice.app.infrastructure.repository.PartRepository;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.PartAllPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PartAllAdapter implements PartAllPort {

    private final PartRepository partRepository;

    @Override
    public List<Part> getAllParts() {
        return partRepository.findAll();
    }
}
