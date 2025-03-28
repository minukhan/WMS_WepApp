package hyundai.purchaseservice.purchase.adapter.in.dto;

import hyundai.purchaseservice.purchase.application.dto.ExpectedQuantityTomorrowDto;

import java.util.List;

public record ExpectedQuantityTomorrowResponse(
        List<ExpectedQuantityTomorrowDto> expectedQuantityList
) {
}
