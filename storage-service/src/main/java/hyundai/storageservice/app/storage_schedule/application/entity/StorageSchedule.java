package hyundai.storageservice.app.storage_schedule.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="storage_schedule")
public class StorageSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_id", nullable = false)
    private String partId; // 이동할 부품 ID

    @Column(name = "part_quantity", nullable = false)
    private int partQuantity; // 이동할 부품 개수

    @Column(name = "storage_quantity", nullable = false)
    private int storageQuantity; // 이동할 부품 개수

    @Column(name = "from_location_section_name", nullable = false)
    private String fromLocationSectionName; // 출고 위치 이름

    @Column(name = "from_location_section_floor", nullable = false)
    private int fromLocationSectionFloor; // 출고 위치 층

    @Column(name = "to_location_section_name", nullable = false)
    private String toLocationSectionName; // 적재 위치 이름

    @Column(name = "to_location_section_floor", nullable = false)
    private int toLocationSectionFloor; // 적재 위치 층

    @Column(name = "scheduled_at", nullable = false)
    private LocalDate scheduledAt; // 적치 일정

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt; // 적치 시작 일정

    @Column(name = "status", nullable = false)
    private String status; // (예: PENDING, COMPLETED)

    public void increaseStorageQuantity() {
        this.storageQuantity += 1;
    }
    public void decreaseStorageQuantity() {
        this.storageQuantity -= 1;
    }
}
