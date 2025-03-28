package hyundai.storageservice.app.storage_schedule.adapter.out;

import hyundai.storageservice.app.infrastructure.repository.StorageScheduleRepository;
import hyundai.storageservice.app.storage.exception.StorageNotFoundException;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import hyundai.storageservice.app.storage_schedule.application.port.out.StorageFilterPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StorageFilterAdapter implements StorageFilterPort {

    private final StorageScheduleRepository storageScheduleRepository;

    @Override
    public List<StorageSchedule> findAll() {

        List<StorageSchedule> storageSchedules = storageScheduleRepository.findAll();

        if(storageSchedules.isEmpty()){
            throw new StorageNotFoundException();
        }
        return storageSchedules;
    }

    @Override
    public List<StorageSchedule> findAllPartId(String partId) {
        List<StorageSchedule> storageSchedules = storageScheduleRepository.findAllByPartIdContaining(partId);

        return storageSchedules;
    }
}
