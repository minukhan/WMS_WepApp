package hyundai.partservice.app.inventory.application.port.out;


import hyundai.partservice.app.inventory.application.entity.Inventory;

public interface InventoryFindByIdPort {

    public abstract Inventory findById(Long id);
}
