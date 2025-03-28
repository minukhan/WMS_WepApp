package hyundai.partservice.app.supplier.application.port.in;

import hyundai.partservice.app.supplier.adapter.dto.SupplierPartResponse;

public interface SupplierFindByNameContainUseCase {

    public abstract SupplierPartResponse findByNameContains(String name);
}
