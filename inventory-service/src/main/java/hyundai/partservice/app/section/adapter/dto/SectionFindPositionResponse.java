package hyundai.partservice.app.section.adapter.dto;

import java.util.List;

public record SectionFindPositionResponse(
    List<SectionQuantityDto> SectionQuantityDtos,
    String PartId
) {
    public static SectionFindPositionResponse of(List<SectionQuantityDto> SectionQuantityDtos, String PartId) {
        return new SectionFindPositionResponse(SectionQuantityDtos, PartId);
    }
}

// 부품이 없을때.