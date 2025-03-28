package hyundai.purchaseservice.purchase.adapter.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record ExpectedQuantityRequest(
        @Schema(description = "품목 코드 리스트")
        List<String> partIds,
        @Schema(description = "원하는 마감 일시")
        LocalDate due
) {
}
