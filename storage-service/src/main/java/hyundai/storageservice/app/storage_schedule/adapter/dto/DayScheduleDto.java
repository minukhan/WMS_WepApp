package hyundai.storageservice.app.storage_schedule.adapter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DayScheduleDto(
        @Schema(description = "품목 명", example = "엔진 오일")
        String partName,
        @Schema(description = "입고 전체 수량", example = "20")
        int requestedQuantity,
        @Schema(description = "입고 완료한 개수", example = "30")
        int currentQuantity
) {


        public static DayScheduleDto of(String partName, int requestedQuantity, int currentQuantity) {
                return new DayScheduleDto(partName, requestedQuantity, currentQuantity);
        }
}
