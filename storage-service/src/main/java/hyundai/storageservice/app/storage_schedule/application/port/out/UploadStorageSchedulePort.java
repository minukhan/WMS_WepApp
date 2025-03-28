package hyundai.storageservice.app.storage_schedule.application.port.out;


import hyundai.storageservice.app.storage.application.entity.Storage;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;

import java.util.List;

public interface UploadStorageSchedulePort {

    public abstract List<Storage> findAll();
    public abstract void save(StorageSchedule storageSchedule);


}
