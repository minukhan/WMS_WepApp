package hyundai.partservice.app.part.adapter.out;


import hyundai.partservice.app.infrastructure.repository.PartRepository;
import hyundai.partservice.app.part.application.port.out.FindPartPort;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.exception.PartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindPartAdapter implements FindPartPort {

    private final PartRepository partRepository;

    @Override
    public Part findPart(String id){
        Part part = partRepository.findById(id).orElseThrow(() -> new PartNotFoundException());
        return part;
    }

}
