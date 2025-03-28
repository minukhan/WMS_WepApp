package hyundai.partservice.app.part.adapter.out;


import hyundai.partservice.app.infrastructure.repository.PartRepository;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.FIndByNameContainPort;
import hyundai.partservice.app.part.exception.PartNameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindByNameContainAdapter implements FIndByNameContainPort {

    private final PartRepository partRepository;

    @Override
    public List<Part> findByNameContains(String name) {

        List<Part> parts = partRepository.findByNameContainingWithSupplier(name);

        if (parts.isEmpty()){
            throw new PartNameNotFoundException();
        }

        return parts;
    }
}
