package hyundai.purchaseservice.purchase.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PURCHASE_SCHEDULE")
public class PurchaseSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer requestedQuantity;

    @Column(nullable = false)
    private Integer processedQuantity;

    @Column(nullable = false)
    private Long sectionId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDate scheduledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private PurchaseRequest purchaseRequest;
}
