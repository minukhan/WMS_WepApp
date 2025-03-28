package hyundai.storageservice.app.storage_schedule.application.port.in;

import hyundai.storageservice.app.storage_schedule.adapter.dto.DayScheduleResponse;

public interface StorageCurrentWorkUseCase {

    public abstract DayScheduleResponse getDaySchedule();
}
