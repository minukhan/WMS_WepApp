package hyundai.safeservice.app.infrastructure;

import hyundai.safeservice.app.safe_Stock.application.entity.SafeStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SafeStockRepository extends JpaRepository<SafeStock, Long> {
    List<SafeStock> findByPartId(String partId);
}
