package hyundai.supplyservice.app.supply.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "supply_part_schedule")
public class SupplyPartSchedule {
    @EmbeddedId
    private SupplyPartScheduleId id; // partId와 출고날짜를 복합키로 사용

    @Column(name = "part_name")
    private String partName;

    @Column(name = "total_requested_quantity", nullable = false)
    private int totalRequestedQuantity;

    @Column(name = "current_quantity", nullable = false)
    private int currentQuantity;

    @OneToMany(mappedBy = "supplyPartSchedule", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<PickingLocation> pickingLocations = new ArrayList<>();

    public SupplyPartSchedule(SupplyPartScheduleId id, String partName, int initialQuantity) {
        this.id = id;
        this.partName = partName;
        this.totalRequestedQuantity = initialQuantity;
        this.currentQuantity = 0;
    }

    public void addQuantity(int quantity) {
        this.totalRequestedQuantity += quantity;
    }

    // qr 스캔으로 현재 출고 수 증가.
    public void updateCurrentQuantity() {
        this.currentQuantity += 1;
    }


}
