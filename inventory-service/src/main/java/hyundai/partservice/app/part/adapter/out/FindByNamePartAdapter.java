package hyundai.partservice.app.part.adapter.out;


import hyundai.partservice.app.infrastructure.repository.PartRepository;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.FindByNamePartPort;
import hyundai.partservice.app.part.exception.PartNameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindByNamePartAdapter implements FindByNamePartPort {

    private final PartRepository partRepository;

    @Override
    public Part findByNamePart(String partName){
        return partRepository.findByName(partName).orElseThrow(()-> new PartNameNotFoundException());
    }


}
