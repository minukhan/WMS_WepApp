package hyundai.partservice.app.inventory.application.entity;


import hyundai.partservice.app.inventory.exception.InventoryOverZeroException;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.exception.SafetyStockOverZeroException;
import hyundai.partservice.app.section.application.entity.Section;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_quantity" ,nullable = false)
    private int partQuantity;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;

    public void updatePartQuantity(int partQuantity) {
        if (partQuantity < 0) {
            throw new InventoryOverZeroException();
        }
        this.partQuantity = partQuantity;
    }



}
