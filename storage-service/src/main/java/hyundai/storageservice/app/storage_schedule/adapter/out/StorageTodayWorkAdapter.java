package hyundai.storageservice.app.storage_schedule.adapter.out;

import hyundai.storageservice.app.infrastructure.repository.StorageScheduleRepository;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import hyundai.storageservice.app.storage_schedule.application.port.out.StorageTodayWorkPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StorageTodayWorkAdapter implements StorageTodayWorkPort {

    private final StorageScheduleRepository storageScheduleRepository;

    @Override
    public List<StorageSchedule> getStorageSchedule() {

        List<StorageSchedule> storageSchedules = storageScheduleRepository.findAll();

        return storageSchedules;
    }
}
