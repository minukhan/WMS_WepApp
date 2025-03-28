package hyundai.safeservice.app.safe_Stock.application.port.out;

import hyundai.safeservice.app.safe_Stock.application.entity.SafeStock;

import java.util.List;

public interface SafeStockFilterPort {
    public abstract List<SafeStock> findByPartId(String partId);
}
