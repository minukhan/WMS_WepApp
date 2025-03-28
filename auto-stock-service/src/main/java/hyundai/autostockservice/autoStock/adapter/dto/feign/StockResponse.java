package hyundai.autostockservice.autoStock.adapter.dto.feign;

import java.util.List;

public record StockResponse(
        Integer total,
        List<PartIdAndQuantityDto> parts
) {
}
