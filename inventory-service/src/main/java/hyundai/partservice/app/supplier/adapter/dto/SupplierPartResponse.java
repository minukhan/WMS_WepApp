package hyundai.partservice.app.supplier.adapter.dto;


import java.util.List;

public record SupplierPartResponse(
        List<SupplierPartDto> supplierPartDtos
) {
    public static SupplierPartResponse from(List<SupplierPartDto> supplierPartDtos) {
        return new SupplierPartResponse(supplierPartDtos);
    }
}
