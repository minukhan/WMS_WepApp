package hyundai.partservice.app.part.application.service;

import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.in.FindByIdModifyOptimalStockUseCase;
import hyundai.partservice.app.part.application.port.out.FindByIdModifySafetyStockAndOptimalStockPort;
import hyundai.partservice.app.part.exception.BelowSafetyStockException;
import hyundai.partservice.app.part.exception.ExceedsMaxStockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindByIdModifyOptimalStockService implements FindByIdModifyOptimalStockUseCase {

    private final FindByIdModifySafetyStockAndOptimalStockPort findByIdModifySafetyStockAndOptimalStockPort;

    @Override
    @Transactional
    public void modifyOptimal(String partId, int optimalStock) {
        Part part = findByIdModifySafetyStockAndOptimalStockPort.findById(partId);

        if(part.getMaxStock() < optimalStock ){
            throw new ExceedsMaxStockException();
        }

        if(part.getSafetyStock() > optimalStock){
            throw new BelowSafetyStockException();
        }

        part.updateOptimalStock(optimalStock);
        findByIdModifySafetyStockAndOptimalStockPort.save(part);
    }
}
