package hyundai.storageservice.app.storage_schedule.adapter.out;

import hyundai.storageservice.app.infrastructure.repository.StorageScheduleRepository;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import hyundai.storageservice.app.storage_schedule.application.port.out.StorePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreAdapter implements StorePort {

    private final StorageScheduleRepository storageScheduleRepository;

    @Override
    public StorageSchedule getStorageSchedule(String partId, String sectionName, int floor) {
        return storageScheduleRepository.findPendingByPartIdAndToLocationSectionNameAndToLocationSectionFloor(partId, sectionName, floor).orElseThrow(null);
    }

    @Override
    public void saveStorageSchedule(StorageSchedule storageSchedule) {
        storageScheduleRepository.save(storageSchedule);
    }
}
