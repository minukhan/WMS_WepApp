package hyundai.safeservice.app.safe_Stock.adapter.out;

import hyundai.safeservice.app.infrastructure.ProprietyStockRepository;
import hyundai.safeservice.app.infrastructure.SafeStockRepository;
import hyundai.safeservice.app.propriety_Stock.application.entity.ProprietyStock;
import hyundai.safeservice.app.safe_Stock.application.entity.SafeStock;
import hyundai.safeservice.app.safe_Stock.application.port.out.ModifyStockPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyStockAdapter implements ModifyStockPort {

    private final SafeStockRepository safeStockRepository;
    private final ProprietyStockRepository proprietyStockRepository;

    @Override
    public void save(SafeStock safeStock) {
        safeStockRepository.save(safeStock);
    }

    @Override
    public void savePro(ProprietyStock proprietyStock) {
        proprietyStockRepository.save(proprietyStock);
    }
}
