package hyundai.partservice.app.part.application.port.in;

import hyundai.partservice.app.part.adapter.dto.PartPurchaseResponse;

public interface FindPartUseCase {
    PartPurchaseResponse findPart(String partId);
}