package hyundai.storageservice.app.storage_schedule.adapter.out;


import hyundai.storageservice.app.infrastructure.repository.StorageRepository;
import hyundai.storageservice.app.infrastructure.repository.StorageScheduleRepository;
import hyundai.storageservice.app.storage.application.entity.Storage;
import hyundai.storageservice.app.storage.exception.StorageNotFoundException;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import hyundai.storageservice.app.storage_schedule.application.port.out.UploadStorageSchedulePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UploadStorageScheduleAdapter implements UploadStorageSchedulePort {

    private final StorageScheduleRepository storageScheduleRepository;
    private final StorageRepository storageRepository;

    @Override
    public List<Storage> findAll() {

        List<Storage> storages = storageRepository.findAll();
        if(storages.isEmpty()){
            throw new StorageNotFoundException();
        }

        return storages;
    }

    @Override
    public void save(StorageSchedule storageSchedule) {
        storageScheduleRepository.save(storageSchedule);
    }
}
