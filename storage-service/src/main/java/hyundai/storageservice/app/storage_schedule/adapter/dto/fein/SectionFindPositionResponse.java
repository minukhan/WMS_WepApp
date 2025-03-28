package hyundai.storageservice.app.storage_schedule.adapter.dto.fein;

import java.util.List;

public record SectionFindPositionResponse(
    List<SectionQuantityDto> SectionQuantityDtos,
    String PartId
) {

}

// 부품이 없을때.