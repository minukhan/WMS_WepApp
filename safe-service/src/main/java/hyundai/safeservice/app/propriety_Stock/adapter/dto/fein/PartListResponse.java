package hyundai.safeservice.app.propriety_Stock.adapter.dto.fein;

import java.util.List;

public record PartListResponse(
        List<PartDto> partGetAllDtos
) {
    public static PartListResponse from(List<PartDto> partGetAllDtos) {
        return new PartListResponse(partGetAllDtos);
    }
}