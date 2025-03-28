package hyundai.storageservice.app.storage_schedule.application.service;

import hyundai.storageservice.app.storage_schedule.adapter.dto.ProgressChartResponse;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import hyundai.storageservice.app.storage_schedule.application.port.in.StorageTodayWorkUseCase;
import hyundai.storageservice.app.storage_schedule.application.port.out.StorageTodayWorkPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StorageTodayWorkService implements StorageTodayWorkUseCase {

    private final StorageTodayWorkPort storageTodayWorkPort;

    @Override
    public ProgressChartResponse getProgressChart() {
        List<StorageSchedule> storageSchedules = storageTodayWorkPort.getStorageSchedule();
        LocalDate today = LocalDate.now();

        // 오늘 날짜와 일치하는 데이터 필터링
        List<StorageSchedule> list = storageSchedules.stream()
                .filter(storageSchedual -> storageSchedual.getScheduledAt().equals(today))
                .toList();

        int todayQuantity =0;
        int currentQuantity =0;

        if (!list.isEmpty()) {

            // 합계 계산
            todayQuantity = list.stream().mapToInt(StorageSchedule::getPartQuantity).sum();
            currentQuantity = list.stream().mapToInt(StorageSchedule::getStorageQuantity).sum();
        }


        return ProgressChartResponse.of(todayQuantity, currentQuantity);
    }
}

