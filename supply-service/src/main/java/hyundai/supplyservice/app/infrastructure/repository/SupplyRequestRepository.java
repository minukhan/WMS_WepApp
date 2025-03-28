package hyundai.supplyservice.app.infrastructure.repository;

import hyundai.supplyservice.app.supply.application.entity.SupplyRequest;
import hyundai.supplyservice.app.supply.application.entity.SupplyRequestPart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SupplyRequestRepository extends JpaRepository<SupplyRequest, Long> {

    @Query("SELECT SUM(srp.quantity) FROM SupplyRequest sr " +
            "JOIN sr.supplyRequestParts srp " +
            "WHERE srp.partId = :partId " +
            "AND sr.deadline >= :startDate " +
            "AND sr.deadline < :endDate " +
            "AND sr.status = 'APPROVED'")
    Long findSupplyRequestsByDeadline(
            @Param("partId") String partId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 주문서 관리
    Page<SupplyRequest> findByStatus(String status, Pageable pageable);
    Page<SupplyRequest> findByUserIdIn(List<Long> userIds, Pageable pageable);
    Page<SupplyRequest> findByUserIdInAndStatus(List<Long> userIds, String status, Pageable pageable);

    List<SupplyRequest> findByDeadlineBetween(LocalDate startDate, LocalDate endDate);
}
