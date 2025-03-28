package site.autoever.verifyservice.verify.adapter.in.dto;

import java.time.LocalDate;
import java.util.List;

public record VerifyOrderRequest(
        long orderId,
        LocalDate orderDate,
        LocalDate dueDate,
        long total,
        List<OrderPartRequest> parts
) {
}
