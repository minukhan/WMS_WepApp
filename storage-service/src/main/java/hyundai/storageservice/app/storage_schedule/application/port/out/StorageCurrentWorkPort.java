package hyundai.storageservice.app.storage_schedule.application.port.out;

import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;

import java.time.LocalDate;
import java.util.List;

public interface StorageCurrentWorkPort {
    public abstract List<StorageSchedule> getCurrentWork(LocalDate date);
}
