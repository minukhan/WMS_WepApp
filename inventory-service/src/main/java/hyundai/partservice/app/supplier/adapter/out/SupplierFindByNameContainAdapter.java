package hyundai.partservice.app.supplier.adapter.out;

import hyundai.partservice.app.infrastructure.repository.SupplierRepositroy;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import hyundai.partservice.app.supplier.application.port.out.SupplierFindByNameContainPort;
import hyundai.partservice.app.supplier.exception.SupplierNameNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SupplierFindByNameContainAdapter implements SupplierFindByNameContainPort {

    private final SupplierRepositroy supplierRepositroy;

    @Override
    public List<Supplier> findByNameContains(String name) {
        List<Supplier> suppliers = supplierRepositroy.findByNameContains(name);

        if(suppliers.isEmpty()) {
            throw new SupplierNameNotFoundException();
        }

        return suppliers;
    }
}
