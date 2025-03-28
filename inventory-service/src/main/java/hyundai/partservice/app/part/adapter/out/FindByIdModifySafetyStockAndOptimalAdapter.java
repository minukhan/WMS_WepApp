package hyundai.partservice.app.part.adapter.out;


import hyundai.partservice.app.infrastructure.repository.PartRepository;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.FindByIdModifySafetyStockAndOptimalStockPort;
import hyundai.partservice.app.part.exception.PartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindByIdModifySafetyStockAndOptimalAdapter implements FindByIdModifySafetyStockAndOptimalStockPort {

    private final PartRepository partRepository;

    @Override
    public Part findById(String partId) {
        return partRepository.findById(partId).orElseThrow(()->new PartNotFoundException());
    }

    @Override
    public void save(Part part) {
        partRepository.save(part);
    }
}
