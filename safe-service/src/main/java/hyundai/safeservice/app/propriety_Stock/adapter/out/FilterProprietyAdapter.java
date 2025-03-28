package hyundai.safeservice.app.propriety_Stock.adapter.out;

import hyundai.safeservice.app.infrastructure.ProprietyStockRepository;
import hyundai.safeservice.app.propriety_Stock.application.entity.ProprietyStock;
import hyundai.safeservice.app.propriety_Stock.application.port.out.FilterProprietyPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FilterProprietyAdapter implements FilterProprietyPort {

    private final ProprietyStockRepository proprietyStockRepository;

    @Override
    public List<ProprietyStock> findBypartId(String partId) {
        return proprietyStockRepository.findByPartId(partId);
    }
}
