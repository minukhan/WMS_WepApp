package site.autoever.verifyservice.verify.application.port.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PartResponseDto(
        @JsonProperty("wareHouseCurrentTotalCount")
        long warehouseCapacity, // JSON 필드명: wareHouseCurrentTotalCount → 필드: warehouseCapacity
        @JsonProperty("maxWareHouseTotal")
        long warehouseMaxCapacity,       // JSON 필드명: maxWareHouseTotal → 필드: warehouseMaxCapacity
        long total,
        @JsonProperty("partAuthenticationDtos")
        List<PartInfoDto> partInfos // JSON 필드명: partAuthenticationDtos → 필드: partInfos
) {
}
