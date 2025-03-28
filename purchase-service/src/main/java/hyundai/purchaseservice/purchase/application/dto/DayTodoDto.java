package hyundai.purchaseservice.purchase.application.dto;

import hyundai.purchaseservice.purchase.adapter.out.dto.GetScheduleResponse;
import hyundai.purchaseservice.purchase.adapter.out.dto.feign.PartWithSupplierDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

public record DayTodoDto(
        @Schema(description = "품목 코드", example = "BHP177")
        String partId,
        @Schema(description = "품목 명", example = "빌트인 캠 유닛")
        String partName,
        @Schema(description = "입고 수량", example = "14")
        Integer quantity,
        @Schema(description = "금액", example = "10000000")
        Long totalPrice,
        @Schema(description = "납품 회사명", example = "삼성SDI")
        String supplier,
        @Schema(description = "입고할 위치명", example = "A구역 1열 1층")
        String section
) {
    public static DayTodoDto of(GetScheduleResponse scheduleDto, PartWithSupplierDto part, String section){
        Long totalPrice = Objects.equals(scheduleDto.totalQuantity(), scheduleDto.requestedQuantity())
                ? scheduleDto.totalPrice()
                : scheduleDto.totalPrice() / scheduleDto.totalQuantity() * scheduleDto.requestedQuantity();

        return new DayTodoDto(
                scheduleDto.partId(),
                part.partName(),
                scheduleDto.requestedQuantity(),
                totalPrice,
                part.supplierName(),
                section
        );
    }

    public static DayTodoDto of(GetScheduleResponse scheduleDto, String partName, String supplier, String section){
        Long totalPrice = Objects.equals(scheduleDto.totalQuantity(), scheduleDto.requestedQuantity())
                ? scheduleDto.totalPrice()
                : scheduleDto.totalPrice() / scheduleDto.totalQuantity() * scheduleDto.requestedQuantity();

        return new DayTodoDto(
                scheduleDto.partId(),
                partName,
                scheduleDto.requestedQuantity(),
                totalPrice,
                supplier,
                section
        );
    }
}
