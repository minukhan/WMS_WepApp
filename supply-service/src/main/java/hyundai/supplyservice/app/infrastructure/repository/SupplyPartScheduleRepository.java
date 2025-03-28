package hyundai.supplyservice.app.infrastructure.repository;

import hyundai.supplyservice.app.supply.application.entity.SupplyPartSchedule;
import hyundai.supplyservice.app.supply.application.entity.SupplyPartScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplyPartScheduleRepository extends JpaRepository<SupplyPartSchedule, SupplyPartScheduleId> {

    @Query("SELECT s FROM SupplyPartSchedule s WHERE s.id.scheduledAt = :date")
    List<SupplyPartSchedule> findAllByDate(@Param("date") LocalDate date);



    @Query("SELECT s FROM SupplyPartSchedule s WHERE s.id.partId = :partId AND s.id.scheduledAt = :date")
    Optional<SupplyPartSchedule> findByPartIdAndDate(@Param("partId") String partId, @Param("date") LocalDate date);

    List<SupplyPartSchedule> findAllByIdScheduledAtBetween(LocalDate startDate, LocalDate endDate);

    // 현재부터 입력된 날까지 총 출고될 수량 계산
    @Query("SELECT SUM(s.totalRequestedQuantity) " +
            "FROM SupplyPartSchedule s " +
            "WHERE s.id.scheduledAt BETWEEN :startDate AND :endDate")
    Long sumTotalRequestedQuantityBetweenDates(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<SupplyPartSchedule> findByIdScheduledAtBetweenAndIdPartId(
            LocalDate startDate,
            LocalDate endDate,
            String partId
    );

}
