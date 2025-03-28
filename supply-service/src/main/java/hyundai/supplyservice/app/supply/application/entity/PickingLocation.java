package hyundai.supplyservice.app.supply.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "picking_location")
public class PickingLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "scheduled_at", referencedColumnName = "scheduled_at"),
            @JoinColumn(name = "part_id", referencedColumnName = "part_id")
    })
    private SupplyPartSchedule supplyPartSchedule;

    private Long sectionId;
    private String sectionName; // A-1 A구역 1열
    private int floor;  // 1,2,3,4,5
    private int plannedQuantity;  // 고정값 - 출고 날짜로 한번만 part에 요청해서 저장
    private int currentQuantity;   // 실제 출고량 (실시간 증가)

    // qr 스캔으로 현재 출고 수 증가.
    public void updateCurrentQuantity() {
        this.currentQuantity += 1;
    }
}
