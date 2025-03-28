package hyundai.purchaseservice.purchase.adapter.in.dto;

import hyundai.purchaseservice.purchase.application.dto.PartQuantityInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ExpectedQuantityResponse (
        @Schema(description = "총 입고 예정 수량")
        Long total,
        List<PartQuantityInfoDto> parts
){
}
