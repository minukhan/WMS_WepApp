package hyundai.supplyservice.app.infrastructure.repository;


import hyundai.supplyservice.app.supply.application.entity.SupplyRequestPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplyRequestPartRepository extends JpaRepository<SupplyRequestPart, Long> {
}
