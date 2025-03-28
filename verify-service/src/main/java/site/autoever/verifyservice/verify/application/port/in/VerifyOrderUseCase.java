package site.autoever.verifyservice.verify.application.port.in;

import site.autoever.verifyservice.verify.adapter.in.dto.VerifyOrderRequest;
import site.autoever.verifyservice.verify.adapter.in.dto.VerifyOrderResponse;

public interface VerifyOrderUseCase {
    VerifyOrderResponse verifyOrder(VerifyOrderRequest request);
}
