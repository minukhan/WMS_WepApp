package hyundai.partservice.app.supplier.adapter.dto;

import hyundai.partservice.app.part.adapter.dto.PartDto;
import hyundai.partservice.app.supplier.application.entity.Supplier;

import java.util.List;

public record SupplierPartDto(
        Long supplierId,
        String supplierName,
        String supplierPhoneNumber,
        String supplierAddress,
        List<PartDto> parts
) {

    public static SupplierPartDto of(Supplier supplier, List<PartDto> partDtos) {

        return new SupplierPartDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getAddress(),
                supplier.getPhoneNumber(),
                partDtos
                );
    }


}
