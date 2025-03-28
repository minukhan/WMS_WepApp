package hyundai.partservice.app.supplier.adapter.out;


import hyundai.partservice.app.infrastructure.repository.SupplierRepositroy;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import hyundai.partservice.app.supplier.application.port.out.FindByIdSupplierPort;
import hyundai.partservice.app.supplier.exception.SupplierIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindByIdSupplierAdapter implements FindByIdSupplierPort {

    private final SupplierRepositroy supplierRepositroy;

    @Override
    public Supplier findById(Long id) {
        return supplierRepositroy.findById(id).orElseThrow(() -> new SupplierIdNotFoundException());
    }
}
