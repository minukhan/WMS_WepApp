package hyundai.storageservice.app.storage_schedule.application.port.out;


import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;

import java.util.List;

public interface StorageFilterPort {
    public abstract List<StorageSchedule> findAll();
    public abstract List<StorageSchedule> findAllPartId(String partId);

 }
