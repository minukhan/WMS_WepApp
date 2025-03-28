package hyundai.partservice.app.part.application.port.in;

import hyundai.partservice.app.part.adapter.dto.PartListPurchaseResponse;

public interface FindByNameContainUseCase {

    public abstract PartListPurchaseResponse findByNameContains(String name);
}
