package hyundai.storageservice.app.storage_schedule.application.port.in;


import hyundai.storageservice.app.storage_schedule.adapter.dto.ProgressChartResponse;

public interface StorageTodayWorkUseCase {
    public ProgressChartResponse getProgressChart();
}
