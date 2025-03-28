package hyundai.partservice.app.part.application.port.in;


import hyundai.partservice.app.part.adapter.dto.PartInventoryResponse;

public interface FIndByPartIdInventoryListUseCase {

    public abstract PartInventoryResponse getPartInventory(String partId);
}
