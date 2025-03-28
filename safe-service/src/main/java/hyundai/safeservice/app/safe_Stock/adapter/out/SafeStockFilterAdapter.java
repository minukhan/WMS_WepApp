package hyundai.safeservice.app.safe_Stock.adapter.out;

import hyundai.safeservice.app.infrastructure.SafeStockRepository;
import hyundai.safeservice.app.safe_Stock.application.entity.SafeStock;
import hyundai.safeservice.app.safe_Stock.application.port.out.SafeStockFilterPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SafeStockFilterAdapter implements SafeStockFilterPort {

    private final SafeStockRepository safeStockRepository;

    @Override
    public List<SafeStock> findByPartId(String partId) {
        return safeStockRepository.findByPartId(partId);
    }
}
