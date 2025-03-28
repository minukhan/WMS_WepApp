package hyundai.storageservice.app.storage_schedule.application.port.out;


import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;

import java.util.List;

public interface MonthSchedulePort {
    public abstract List<StorageSchedule> getStorageSchedule();
}
