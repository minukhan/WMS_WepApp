package hyundai.storageservice.app.storage_schedule.adapter.dto.fein;


public record PartPurchaseResponse(
        PartSupplierDto partSupplierDto
) {

    public static PartPurchaseResponse from(PartSupplierDto partSupplierDto) {
        return new PartPurchaseResponse(partSupplierDto);
    }
}
