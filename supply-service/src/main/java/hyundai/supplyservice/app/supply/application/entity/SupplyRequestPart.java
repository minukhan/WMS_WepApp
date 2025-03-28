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
@Table(name = "supply_request_part")
public class SupplyRequestPart {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "part_id" ,nullable = false)// part_id 연관 관계 설정해야함.
    private String partId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity" ,nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "supply_request_id", nullable = false)
    private SupplyRequest supplyRequest;

    public SupplyRequestPart(String partId, int quantity) {
        this.partId = partId;
        this.quantity = quantity;
    }

}
