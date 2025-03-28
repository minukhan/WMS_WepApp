package hyundai.storageservice.app.storage_schedule.application.port.in;


import hyundai.storageservice.app.storage_schedule.adapter.dto.WorkerDayScheduleResponse;

public interface StorageTodayWorkWithDetailUseCase {
    public abstract WorkerDayScheduleResponse getWorkerDaySchedule();
}
