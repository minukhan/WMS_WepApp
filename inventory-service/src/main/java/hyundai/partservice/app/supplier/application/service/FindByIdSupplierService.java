package hyundai.partservice.app.supplier.application.service;


import hyundai.partservice.app.supplier.adapter.dto.SupplierDto;
import hyundai.partservice.app.supplier.adapter.dto.SupplierResponse;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import hyundai.partservice.app.supplier.application.port.in.FindByIdSupplierUseCase;
import hyundai.partservice.app.supplier.application.port.out.FindByIdSupplierPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindByIdSupplierService implements FindByIdSupplierUseCase {

    private final FindByIdSupplierPort findByIdSupplierPort;

    @Override
    public SupplierResponse findById(Long id) {
        Supplier supplier = findByIdSupplierPort.findById(id);
        SupplierDto supplierDto = SupplierDto.from(supplier);

        return SupplierResponse.from(supplierDto);
    }

}
