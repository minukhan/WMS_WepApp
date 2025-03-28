package hyundai.partservice.app.section.application.entity;


import hyundai.partservice.app.inventory.application.entity.Inventory;
import hyundai.partservice.app.inventory.exception.InventoryOverZeroException;
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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "floor", nullable = false)
    private int floor;

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @OneToMany(mappedBy = "section", fetch = FetchType.EAGER)
    private List<Inventory> inventories = new ArrayList<>();

    public void updateQuantity(int quantity) {
        if (quantity < 0) {
            throw new InventoryOverZeroException();
        }
        this.quantity = quantity;
    }


}
