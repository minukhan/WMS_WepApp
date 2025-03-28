package hyundai.safeservice.app.safe_Stock.adapter.dto;

import java.util.List;

public record PartSafeListResponse(
        List<PartInfoWithAIDto> partInfoWithAIDtoList
) {
    public static PartSafeListResponse from(List<PartInfoWithAIDto> partInfoWithAIDtoList) {
        return new PartSafeListResponse(partInfoWithAIDtoList);
    }
}
