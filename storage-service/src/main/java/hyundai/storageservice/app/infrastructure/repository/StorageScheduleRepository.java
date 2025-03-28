package hyundai.storageservice.app.infrastructure.repository;

import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StorageScheduleRepository extends JpaRepository<StorageSchedule,Long>{

    @Query("SELECT s FROM StorageSchedule s WHERE s.scheduledAt = :today")
    List<StorageSchedule> findByScheduledDate(@Param("today") LocalDate today);

    @Query("SELECT s FROM StorageSchedule s WHERE s.partId = :partId AND s.toLocationSectionName = :toLocationSectionName AND s.toLocationSectionFloor = :toLocationSectionFloor AND s.status = 'PENDING'")
    Optional<StorageSchedule> findPendingByPartIdAndToLocationSectionNameAndToLocationSectionFloor(
            @Param("partId") String partId,
            @Param("toLocationSectionName") String toLocationSectionName,
            @Param("toLocationSectionFloor") int toLocationSectionFloor
    );

    List<StorageSchedule> findAllByPartIdContaining(String partId);
}
