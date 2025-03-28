package hyundai.partservice.app.inventory.application.port.in;


import hyundai.partservice.app.inventory.adapter.dto.InventoryResponse;

public interface InventoryFindByIdUseCase {

    public abstract InventoryResponse findById(Long id);
}
