package hyundai.partservice.app.supplier.adapter.dto;

public record SupplierResponse(
        SupplierDto supplierDto
) {
    public static SupplierResponse from(SupplierDto supplierDto) {
        return new SupplierResponse(supplierDto);
    }
}
