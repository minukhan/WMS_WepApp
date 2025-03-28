package hyundai.purchaseservice.infrastructure.repository;

import hyundai.purchaseservice.purchase.application.entity.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long> {

}
