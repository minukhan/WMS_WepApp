package hyundai.purchaseservice.purchase.application.port.in;

import hyundai.purchaseservice.purchase.adapter.in.dto.RegisterAutoRequest;
import hyundai.purchaseservice.purchase.adapter.in.dto.RegisterRequest;

public interface RegisterRequestUseCase {
    void registerRequest(RegisterRequest registerRequest);
    void registerAutoRequest(RegisterAutoRequest registerAutoRequest);
}
