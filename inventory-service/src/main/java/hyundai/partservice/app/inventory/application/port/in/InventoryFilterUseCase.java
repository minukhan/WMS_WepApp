package hyundai.partservice.app.inventory.application.port.in;


import hyundai.partservice.app.inventory.adapter.dto.InventoryFilterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryFilterUseCase {

    public abstract InventoryFilterResponse filter(String partId,
                                                         String partName,
                                                         String location,
                                                         String supplier,
                                                         String currentQuantity,
                                                         String safetyQuantity,
                                                         Pageable pageable);
}
