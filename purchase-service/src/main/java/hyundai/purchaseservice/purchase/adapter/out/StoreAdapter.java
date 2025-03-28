package hyundai.purchaseservice.purchase.adapter.out;

import hyundai.purchaseservice.infrastructure.mapper.ScheduleMapper;
import hyundai.purchaseservice.purchase.adapter.out.dto.ScheduleIdAndQuantityResponse;
import hyundai.purchaseservice.purchase.application.port.out.StorePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreAdapter implements StorePort {
    private final ScheduleMapper scheduleMapper;

    @Override
    public List<ScheduleIdAndQuantityResponse> checkProcessedQuantity(String partId, Long sectionId) {
        return scheduleMapper.checkProcessedQuantity(partId, sectionId);
    }

    @Override
    public void addProcessedQuantity(ScheduleIdAndQuantityResponse updateSection) {
        scheduleMapper.addProcessedQuantity(updateSection.id(), updateSection.processedQuantity()+1, updateSection.requestId());
    }

    @Override
    public void checkAndUpdateRequestStatus(Long requestId) {
        scheduleMapper.checkWorkingRequest(requestId);
    }

}
