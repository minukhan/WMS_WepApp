package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

import java.util.List;

public record SupplierPartDto(
        Long supplierId,
        String supplierName,
        String supplierPhoneNumber,
        String supplierAddress,
        List<PartDto> parts
) {
}
