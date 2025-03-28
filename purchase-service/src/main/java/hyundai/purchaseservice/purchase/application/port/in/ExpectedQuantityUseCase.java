package hyundai.purchaseservice.purchase.application.port.in;

import hyundai.purchaseservice.purchase.adapter.in.dto.ExpectedQuantityRequest;
import hyundai.purchaseservice.purchase.adapter.in.dto.ExpectedQuantityResponse;
import hyundai.purchaseservice.purchase.adapter.in.dto.ExpectedQuantityTomorrowResponse;

import java.time.LocalDate;
import java.util.List;

public interface ExpectedQuantityUseCase {
    ExpectedQuantityResponse prefixSumStockAmount(ExpectedQuantityRequest request);

    ExpectedQuantityTomorrowResponse getTomorrowStockList();
}
