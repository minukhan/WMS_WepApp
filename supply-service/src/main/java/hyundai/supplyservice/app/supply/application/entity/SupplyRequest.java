package hyundai.supplyservice.app.supply.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "supply_request")
public class SupplyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "requested_at", nullable = false)
    private LocalDate requestedAt;

    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name="message", nullable = false)
    private String message;

    @OneToOne(mappedBy = "supplyRequest", cascade = CascadeType.ALL)
    private SupplySchedule supplySchedule;

    @OneToMany(mappedBy = "supplyRequest", cascade = CascadeType.ALL)
    private List<SupplyRequestPart> supplyRequestParts = new ArrayList<>();

    public void updateStatus(String status, String message) {
        this.status = status;
        this.message = message;
    }

}
