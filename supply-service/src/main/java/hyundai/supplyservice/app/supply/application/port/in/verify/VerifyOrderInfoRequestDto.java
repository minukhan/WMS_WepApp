package hyundai.supplyservice.app.supply.application.port.in.verify;

import hyundai.supplyservice.app.supply.application.port.in.commondto.PartCountDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record VerifyOrderInfoRequestDto(
        @Schema(description = "주문서 ID", example = "6")
        Long orderId,
        @Schema(description = "주문 날짜", example = "2024-02-10")
        LocalDate orderDate,
        @Schema(description = "요청 마감 기한", example = "2024-02-20")
        LocalDate dueDate,
        @Schema(description = "총 부품 개수", example = "500")
        int total,
        @Schema(description = "주문 부품 리스트")
        List<PartCountDto> parts

) {

}
