package hyundai.purchaseservice.purchase.application.port.out;

import hyundai.purchaseservice.purchase.adapter.out.dto.ScheduleIdAndQuantityResponse;

import java.util.List;

public interface StorePort {
    List<ScheduleIdAndQuantityResponse> checkProcessedQuantity(String partId, Long sectionId);

    void addProcessedQuantity(ScheduleIdAndQuantityResponse updateSection);

    void checkAndUpdateRequestStatus(Long requestId);
}
