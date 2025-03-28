package hyundai.partservice.app.supplier.application.port.out;

import hyundai.partservice.app.supplier.application.entity.Supplier;

import java.util.List;

public interface SupplierFindByNameContainPort {

    public abstract List<Supplier> findByNameContains(String name);
}
