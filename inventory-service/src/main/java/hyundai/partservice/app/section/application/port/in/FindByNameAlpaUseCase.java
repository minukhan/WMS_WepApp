package hyundai.partservice.app.section.application.port.in;


import hyundai.partservice.app.inventory.adapter.dto.InventoryListResponse;
import hyundai.partservice.app.section.adapter.dto.SectionInventoryResponse;

public interface FindByNameAlpaUseCase {
    public abstract InventoryListResponse findByAlpa(String alpa);
}
