package hyundai.partservice.app.inventory.application.port.in;



public interface InventoryDecrementPartQuantityUseCase {
    public abstract void decrementPartQuantity(String partId,String sectionName, int floor);
}
