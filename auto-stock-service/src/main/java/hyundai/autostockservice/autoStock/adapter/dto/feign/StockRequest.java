package hyundai.autostockservice.autoStock.adapter.dto.feign;

import java.time.LocalDate;
import java.util.List;

public record StockRequest(
        LocalDate dueDate,
        List<String> partIds
) {
}
