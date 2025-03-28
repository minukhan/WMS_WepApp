package hyundai.partservice.app.supplier.application.port.in;


import hyundai.partservice.app.supplier.adapter.dto.SupplierResponse;

public interface FindByIdSupplierUseCase {

    public abstract SupplierResponse findById(Long id);
}
