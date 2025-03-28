package hyundai.purchaseservice.infrastructure.repository;

import hyundai.purchaseservice.purchase.application.entity.PurchaseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseScheduleRepository extends JpaRepository<PurchaseSchedule, Long> {

}
