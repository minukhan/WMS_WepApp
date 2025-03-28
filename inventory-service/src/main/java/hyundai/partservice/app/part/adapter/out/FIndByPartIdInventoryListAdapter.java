package hyundai.partservice.app.part.adapter.out;

import hyundai.partservice.app.infrastructure.repository.PartRepository;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.FIndByPartIdInventoryListPort;
import hyundai.partservice.app.part.exception.PartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FIndByPartIdInventoryListAdapter implements FIndByPartIdInventoryListPort {

    private final PartRepository partRepository;

    @Override
    public Part findPartByPartIdInventory(String partId) {

        Part part = partRepository.findByIdWithInventoryList(partId).orElseThrow(()-> new PartNotFoundException());
        return part;
    }
}
