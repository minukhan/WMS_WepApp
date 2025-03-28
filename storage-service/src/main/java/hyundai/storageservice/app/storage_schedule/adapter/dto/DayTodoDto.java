package hyundai.storageservice.app.storage_schedule.adapter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record DayTodoDto(
        @Schema(description = "품목 코드", example = "BHP177")
        String partId,
        @Schema(description = "품목 명", example = "빌트인 캠 유닛")
        String partName,
        @Schema(description = "입고 수량", example = "14")
        int quantity,
        @Schema(description = "금액", example = "10000000") // 총금액
        int totalPrice,
        @Schema(description = "현재 구역", example = "X구역 1열 1층")
        String currentSection,
        @Schema(description = "입고할 위치명", example = "A구역 2열 3층")
        String moveSection
) {

    public static DayTodoDto of(String partId,String partName, int quantity,int totalPrice, String currentSection, String moveSection){
        return new DayTodoDto(partId,partName,quantity,totalPrice,currentSection,moveSection);
    }


}
