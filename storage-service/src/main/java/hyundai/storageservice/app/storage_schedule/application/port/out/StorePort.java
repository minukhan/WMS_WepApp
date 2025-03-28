package hyundai.storageservice.app.storage_schedule.application.port.out;

import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;

public interface StorePort {
    public abstract StorageSchedule getStorageSchedule(String partId, String sectionName, int floor);
    public abstract void saveStorageSchedule(StorageSchedule storageSchedule);
}
