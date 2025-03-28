package hyundai.partservice.app.supplier.application.port.out;


import hyundai.partservice.app.supplier.application.entity.Supplier;

public interface FindByIdSupplierPort {
    public abstract Supplier findById(Long supplierId);

}
