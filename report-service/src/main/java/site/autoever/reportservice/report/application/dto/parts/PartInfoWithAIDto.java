package site.autoever.reportservice.report.application.dto.parts;

public record PartInfoWithAIDto(
        String partId, //부품 ID
        String partName, // 부품 Name
        long currentTotalQuantity, // 전월 출고량
        long aiSafetyQuantity, // 예측 출고량
        long currentSafetyQuantity, // 현재 안전수량
        long recommendSafetyQuantity, // 추천 안전수량
        long currentProprietyQuantity, // 현재 적정수량
        long recommendProprietyQuantity // 추천 적정수량
) {

}