package hyundai.partservice.app.part.application.entity;

import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.part.exception.OptimalStockOverZeroException;
import hyundai.partservice.app.part.exception.SafetyStockOverZeroException;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "part")
public class Part {

    @Id
    @Column(unique = true, nullable = false)
    private String id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="quantity", nullable = false)
    private int quantity;

    @Column(name="safety_stock", nullable = false)
    private int safetyStock;

    @Column(name="max_stock", nullable = false)
    private int maxStock;

    @Column(name="optimal_stock", nullable = false)
    private int optimalStock;

    @Column(name="delivery_duration", nullable = false)
    private int deliveryDuration;

    @Column(name="price", nullable = false)
    private Long price;

    @Column(name = "category", nullable = false)
    private String category;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL)
    private List<Inventory> inventoryList = new ArrayList<>();

    public void updateSafetyStock(int safetyStock) {
        if (safetyStock < 0) {
            throw new SafetyStockOverZeroException();
        }
        this.safetyStock = safetyStock;
    }

    public void updateOptimalStock(int optimalStock) {
        if (optimalStock < 0) {
            throw new OptimalStockOverZeroException();
        }
        this.optimalStock = optimalStock;
    }

    public void updateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("부품 수량은 0 이상이어야 합니다.");
        }
        this.quantity = quantity;
    }


}
