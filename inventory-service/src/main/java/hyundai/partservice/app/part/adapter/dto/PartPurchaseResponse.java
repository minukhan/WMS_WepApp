package hyundai.partservice.app.part.adapter.dto;


public record PartPurchaseResponse(
        PartSupplierDto partSupplierDto
) {

    public static PartPurchaseResponse from(PartSupplierDto partSupplierDto) {
        return new PartPurchaseResponse(partSupplierDto);
    }
}
