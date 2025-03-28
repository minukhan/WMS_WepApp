package hyundai.partservice.app.part.adapter.out;


import hyundai.partservice.app.infrastructure.repository.InventoryRepository;
import hyundai.partservice.app.infrastructure.repository.PartRepository;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.FindByPartIdAuthenticationPort;
import hyundai.partservice.app.part.exception.PartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindByPartIdAuthenticationAdapter implements FindByPartIdAuthenticationPort {

    private final PartRepository partRepository;
    private final InventoryRepository inventoryRepository;
    @Override
    public Part findByPartId(String partId) {

        Part part = partRepository.findById(partId).orElseThrow(()-> new PartNotFoundException());
        return part;
    }

    @Override
    public int partCount(String partId) {
        return partRepository.getTotalQuantityByPartId(partId);
    }
    @Override
    public int currentTotalCount() {
        return inventoryRepository.getCurrentTotalCount();
    }
}
