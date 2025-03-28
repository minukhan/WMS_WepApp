package hyundai.partservice.app.supplier.application.service;


import hyundai.partservice.app.part.adapter.dto.PartDto;
import hyundai.partservice.app.supplier.adapter.dto.SupplierPartDto;
import hyundai.partservice.app.supplier.adapter.dto.SupplierPartResponse;
import hyundai.partservice.app.supplier.application.entity.Supplier;
import hyundai.partservice.app.supplier.application.port.in.SupplierFindByNameContainUseCase;
import hyundai.partservice.app.supplier.application.port.out.SupplierFindByNameContainPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierFindByNameContainService implements SupplierFindByNameContainUseCase {

    private final SupplierFindByNameContainPort findByNameContainPort;

    @Override
    public SupplierPartResponse findByNameContains(String name) {


        List<Supplier> suppliers = findByNameContainPort.findByNameContains(name);

        List<SupplierPartDto> supplierPartDtos = suppliers.stream()
                .map((supplier -> SupplierPartDto.of(
                        supplier,
                        supplier.getParts()
                                .stream().map(part -> PartDto.from(part))
                                .collect(Collectors.toList())
                )))
                .collect(Collectors.toList());

        return SupplierPartResponse.from(supplierPartDtos);
    }
}
