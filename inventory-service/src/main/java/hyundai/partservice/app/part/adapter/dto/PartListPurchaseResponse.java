package hyundai.partservice.app.part.adapter.dto;


import java.util.List;

public record PartListPurchaseResponse(
        List<PartSupplierDto> partDtos
) {
    public static PartListPurchaseResponse from(List<PartSupplierDto> partSupplierDtos) {
        return new PartListPurchaseResponse(partSupplierDtos);
    }
}
