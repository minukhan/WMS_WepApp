package hyundai.storageservice.util;

import hyundai.storageservice.app.storage_schedule.application.port.in.UploadStorageScheduleUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class StorageScheduleScheduler {

    private final UploadStorageScheduleUseCase uploadStorageScheduleUseCase;

    // 예: 매일 새벽 2시에 실행
    @Scheduled(cron = "0 0 0 7,21 * ?")
    public void executeUploadStorageSchedule() {
        log.info("UploadStorageSchedule 실행 시작");
        uploadStorageScheduleUseCase.uploadStorageScheduleUseCase();
        log.info("UploadStorageSchedule 실행 완료");
    }
}
