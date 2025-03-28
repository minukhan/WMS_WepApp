package hyundai.safeservice.app.propriety_Stock.adapter.out;

import hyundai.safeservice.app.infrastructure.ProprietyStockRepository;
import hyundai.safeservice.app.propriety_Stock.application.entity.ProprietyStock;
import hyundai.safeservice.app.propriety_Stock.application.port.out.RegistrationStockProprietyPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegistrationStockProprietyAdapter implements RegistrationStockProprietyPort {

    private final ProprietyStockRepository proprietyStockRepository;

    @Override
    public void stockSave(ProprietyStock proprietyStock) {
        proprietyStockRepository.save(proprietyStock);
    }
}
