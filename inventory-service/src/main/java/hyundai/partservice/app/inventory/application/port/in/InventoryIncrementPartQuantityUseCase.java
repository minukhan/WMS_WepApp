package hyundai.partservice.app.inventory.application.port.in;

public interface InventoryIncrementPartQuantityUseCase {
    public abstract void incrementPartQuantity(String partId, int floor, String sectionName);
}
