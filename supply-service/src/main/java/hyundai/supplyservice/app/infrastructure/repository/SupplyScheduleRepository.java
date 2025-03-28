package hyundai.supplyservice.app.infrastructure.repository;

import hyundai.supplyservice.app.supply.application.entity.SupplySchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SupplyScheduleRepository extends JpaRepository<SupplySchedule, Long> {
    List<SupplySchedule> findByScheduledAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    @Query("SELECT DISTINCT CAST(DATE(s.scheduledAt) AS LocalDate) FROM SupplySchedule s WHERE YEAR(s.scheduledAt) = :year AND MONTH(s.scheduledAt) = :month")
    List<LocalDate> findDistinctDatesByYearAndMonth(@Param("year") int year, @Param("month") int month);
}
