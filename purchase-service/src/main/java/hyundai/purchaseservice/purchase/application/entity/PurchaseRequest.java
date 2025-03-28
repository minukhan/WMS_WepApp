package hyundai.purchaseservice.purchase.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PURCHASE_REQUEST")
public class PurchaseRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String partId;  // 부품 id

    @Column(nullable = false)
    private Integer quantity;   // 부품 수량

    private String requesterName;   // 요청자 이름

    private LocalDate orderedAt;    // 주문 시간

    private LocalDate expectedDeliveryDate; // 마감일자

    @Column(nullable = false)
    private Long totalPrice;    // 전체 금액

    @Column(nullable = false)
    private String status;  // 주문 상태(요청 중, 완료)

    @OneToMany(mappedBy = "purchaseRequest", cascade = CascadeType.REMOVE)
    List<PurchaseSchedule> purchaseSchedules;
}
