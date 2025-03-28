package hyundai.autostockservice.autoStock.adapter.dto.feign;

import java.util.List;

public record PurchaseRequestSaveRequest(
        List<PartIdAndQuantityDto> requests
) {
}
