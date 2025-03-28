package hyundai.purchaseservice.purchase.adapter.out.dto.feign;

import java.time.LocalDate;
import java.util.List;

public record ExpectedStockRequest(
        LocalDate dueDate,
        List<String> partIds
) {
}
