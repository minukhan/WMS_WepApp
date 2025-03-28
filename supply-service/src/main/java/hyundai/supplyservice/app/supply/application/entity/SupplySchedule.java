package hyundai.supplyservice.app.supply.application.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "supply_schedule")
public class SupplySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDate scheduledAt;

    @OneToOne
    @JoinColumn(name = "supply_request_id")
    private SupplyRequest supplyRequest;
}
