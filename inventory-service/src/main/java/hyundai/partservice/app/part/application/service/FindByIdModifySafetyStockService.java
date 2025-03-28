package hyundai.partservice.app.part.application.service;

import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.in.FindByIdModifySafetyStockUseCase;
import hyundai.partservice.app.part.application.port.out.FindByIdModifySafetyStockAndOptimalStockPort;
import hyundai.partservice.app.part.exception.ExceedsOptimalStockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindByIdModifySafetyStockService implements FindByIdModifySafetyStockUseCase {

    private final FindByIdModifySafetyStockAndOptimalStockPort findByIdModifySafetyStockPort;

    @Override
    @Transactional
    public void modifySafetyStock(String partId, int safetyStock) {

        Part part = findByIdModifySafetyStockPort.findById(partId);

        if(safetyStock > part.getOptimalStock()){
            throw new ExceedsOptimalStockException();
        }

        part.updateSafetyStock(safetyStock);
        findByIdModifySafetyStockPort.save(part);
    }
}
