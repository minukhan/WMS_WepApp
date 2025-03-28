package hyundai.safeservice.app.infrastructure;

import hyundai.safeservice.app.propriety_Stock.application.entity.ProprietyStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProprietyStockRepository extends JpaRepository<ProprietyStock, Long> {
    List<ProprietyStock> findByPartId(String partId);
}
