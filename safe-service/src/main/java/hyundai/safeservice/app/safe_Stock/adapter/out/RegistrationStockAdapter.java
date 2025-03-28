package hyundai.safeservice.app.safe_Stock.adapter.out;

import hyundai.safeservice.app.infrastructure.SafeStockRepository;
import hyundai.safeservice.app.propriety_Stock.application.port.out.RegistrationStockProprietyPort;
import hyundai.safeservice.app.safe_Stock.application.entity.SafeStock;
import hyundai.safeservice.app.safe_Stock.application.port.out.RegistrationStockPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationStockAdapter implements RegistrationStockPort {

    private final SafeStockRepository safeStockRepository;

    @Override
    public void stockSave(SafeStock safeStock) {
        safeStockRepository.save(safeStock);
    }
}
