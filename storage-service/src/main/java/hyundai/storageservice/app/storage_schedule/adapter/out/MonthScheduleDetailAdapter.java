package hyundai.storageservice.app.storage_schedule.adapter.out;

import hyundai.storageservice.app.infrastructure.repository.StorageScheduleRepository;
import hyundai.storageservice.app.storage.exception.StorageNotFoundException;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import hyundai.storageservice.app.storage_schedule.application.port.out.MonthScheduleDetailPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MonthScheduleDetailAdapter implements MonthScheduleDetailPort {

    private final StorageScheduleRepository storageScheduleRepository;

    @Override
    public List<StorageSchedule> getStorageSchedule() {

        List<StorageSchedule> storageSchedules = storageScheduleRepository.findAll();

        if(storageSchedules == null){
             throw new StorageNotFoundException();
        }
        return storageSchedules;
    }
}
