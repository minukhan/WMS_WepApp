package hyundai.partservice.app.supplier.adapter.dto;


import hyundai.partservice.app.supplier.application.entity.Supplier;

public record SupplierDto(
        Long supplierId,
        String supplierName,
        String address,
        String phoneNumber
) {
    public static SupplierDto from(Supplier supplier) {
        return new SupplierDto(
                supplier.getId(),
                supplier.getName(),
                supplier.getAddress(),
                supplier.getPhoneNumber()
        );
    }
}
