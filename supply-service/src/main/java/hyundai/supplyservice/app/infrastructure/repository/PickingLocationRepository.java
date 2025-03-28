package hyundai.supplyservice.app.infrastructure.repository;

import hyundai.supplyservice.app.supply.application.entity.PickingLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PickingLocationRepository extends JpaRepository<PickingLocation, Long> {
    @Query("SELECT p FROM PickingLocation p " +
            "WHERE p.supplyPartSchedule.id.partId = :partId " +
            "AND p.sectionName = :sectionName " +
            "AND p.floor = :floor " +
            "AND p.supplyPartSchedule.id.scheduledAt = :scheduledAt")
    Optional<PickingLocation> findByPartIdAndSectionNameAndFloorAndScheduledDate(
            @Param("partId") String partId,
            @Param("sectionName") String sectionName,
            @Param("floor") int floor,
            @Param("scheduledAt") LocalDate scheduledAt
    );

}
