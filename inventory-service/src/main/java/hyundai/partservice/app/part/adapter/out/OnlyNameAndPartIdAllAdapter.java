package hyundai.partservice.app.part.adapter.out;


import hyundai.partservice.app.infrastructure.repository.PartRepository;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.OnlyNameAndPartIdAllPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OnlyNameAndPartIdAllAdapter implements OnlyNameAndPartIdAllPort {

    private final PartRepository partRepository;

    @Override
    public List<Part> getAllParts() {

        List<Part> parts =  partRepository.findAll();
        return parts;
    }
}
