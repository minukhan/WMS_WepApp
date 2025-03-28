package hyundai.purchaseservice.purchase.adapter.in.dto;

import hyundai.purchaseservice.purchase.application.dto.FrequencyDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record FrequencyResponse(
        @Schema(description = "차트의 x축에 나타나는 정보. ㄱㄴㄷ 순으로 정렬.", example = "[\"거리 경고 센서\", \"기타\"]")
        List<String> frequencyItemNameList,
        List<FrequencyDto> frequencyList
) {
}
