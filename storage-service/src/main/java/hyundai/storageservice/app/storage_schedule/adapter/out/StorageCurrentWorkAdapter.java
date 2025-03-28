package hyundai.storageservice.app.storage_schedule.adapter.out;

import hyundai.storageservice.app.infrastructure.repository.StorageScheduleRepository;
import hyundai.storageservice.app.storage_schedule.application.entity.StorageSchedule;
import hyundai.storageservice.app.storage_schedule.application.port.out.StorageCurrentWorkPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StorageCurrentWorkAdapter implements StorageCurrentWorkPort {

    private final StorageScheduleRepository storageScheduleRepository;

    @Override
    public List<StorageSchedule> getCurrentWork(LocalDate date) {

        List<StorageSchedule> storageSchedules = storageScheduleRepository.findByScheduledDate(date);

        return storageSchedules;
    }
}
